package hu.kope.jwtlogin.views.login

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import hu.kope.jwtlogin.R
import hu.kope.jwtlogin.interactors.communication.IRestService
import okhttp3.internal.http.HTTP_UNAUTHORIZED
import okhttp3.internal.http.HTTP_UNAVAILABLE
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.concurrent.TimeUnit


@RunWith(AndroidJUnit4ClassRunner::class)
class LoginActivityTest  {
    private lateinit var server: MockWebServer
    private lateinit var backendService: IRestService

    companion object {
        private val REFRESH_TOKEN =
            "eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2NjM3NDg1NDl9.XDXI6avCtfMkSZYQIwfGdUWXjm2OFGqzvx6cW9mtz9E"

        private const val INVALID = "invalid"
        private const val ERROR = "error"
    }

    @Before
    fun setup() {

        server = MockWebServer()
        server.url("http://test.com")
        backendService = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addConverterFactory(JacksonConverterFactory.create())
            .build()
            .create(IRestService::class.java)

        server.start(8000)


        val activityScenario = ActivityScenario.launch(LoginActivity::class.java)
    }

    private fun enqueueMockResponse(unexpectedError: Boolean) {
        val mockResponse = MockResponse()
        if (unexpectedError){
            mockResponse.http2ErrorCode = HTTP_UNAVAILABLE
        } else {
            mockResponse.http2ErrorCode = HTTP_UNAUTHORIZED
        }
        mockResponse.setBodyDelay(2, TimeUnit.SECONDS)
        server.enqueue(mockResponse)
    }

    @After
    fun tearDown() {
        server.shutdown()

    }


    @Test
    fun test_Login_Button_inactive() {
        onView(withId(R.id.login))
            .check(ViewAssertions.matches(isDisplayed()))
            .check(ViewAssertions.matches(not(isEnabled())))

    }

    @Test
    fun test_fill_login_form_flow() {

        onView(withId(R.id.username))
            .check(ViewAssertions.matches(isDisplayed()))
        //fill username
        onView(withId(R.id.username))
            .perform(ViewActions.replaceText("userName"))
        //button disabled
        onView(withId(R.id.login))
            .check(ViewAssertions.matches(isDisplayed()))
            .check(ViewAssertions.matches(not(isEnabled())))
        //password missing
        onView(withId(R.id.password))
            .check(ViewAssertions.matches(isDisplayed()))
            .check(ViewAssertions.matches(hasErrorText("Missing password")))
        //fill password
        onView(withId(R.id.password))
            .perform(ViewActions.replaceText("password"))
        //button enabled
        onView(withId(R.id.login))
            .check(ViewAssertions.matches(isDisplayed()))
            .check(ViewAssertions.matches(isEnabled()))
        //remove username
        onView(withId(R.id.username))
            .perform(ViewActions.replaceText(""))
        //missing username
        onView(withId(R.id.username))
            .check(ViewAssertions.matches(isDisplayed()))
            .check(ViewAssertions.matches(hasErrorText("Missing username")))
        //button disabled
        onView(withId(R.id.login))
            .check(ViewAssertions.matches(isDisplayed()))
            .check(ViewAssertions.matches(not(isEnabled())))
        //fill username
        onView(withId(R.id.username))
            .perform(ViewActions.replaceText("userName"))
        //button enabled
        onView(withId(R.id.login))
            .check(ViewAssertions.matches(isDisplayed()))
            .check(ViewAssertions.matches(isEnabled()))
    }

    @Test
    fun test_fill_login_invalid_credentials() {
        enqueueMockResponse(false)
        onView(withId(R.id.username))
            .check(ViewAssertions.matches(isDisplayed()))
        onView(withId(R.id.username))
            .perform(ViewActions.replaceText(INVALID))
        onView(withId(R.id.password))
            .check(ViewAssertions.matches(isDisplayed()))
        onView(withId(R.id.password))
            .perform(ViewActions.replaceText(INVALID))
        onView(withId(R.id.login))
            .check(ViewAssertions.matches(isDisplayed()))

        onView(withId(R.id.login))
            .check(ViewAssertions.matches(isDisplayed())).perform().perform(
                ViewActions.click()
            )

        Thread.sleep(2000)

        onView(withText(R.string.text_invalid_username_or_password))
            .check(
                ViewAssertions.matches(
                    withEffectiveVisibility(
                        Visibility.VISIBLE
                    )
                )
            );
    }

    @Test
    fun test_fill_login_try_login_unexpected_error() {
        enqueueMockResponse(true)
        onView(withId(R.id.username))
            .check(ViewAssertions.matches(isDisplayed()))
        onView(withId(R.id.username))
            .perform(ViewActions.replaceText(ERROR))
        onView(withId(R.id.password))
            .check(ViewAssertions.matches(isDisplayed()))
        onView(withId(R.id.password))
            .perform(ViewActions.replaceText(ERROR))
        onView(withId(R.id.login))
            .check(ViewAssertions.matches(isDisplayed()))

        onView(withId(R.id.login))
            .check(ViewAssertions.matches(isDisplayed())).perform().perform(
                ViewActions.click()
            )

        Thread.sleep(2000)

        onView(withText(R.string.text_unexpected_error))
            .check(
                ViewAssertions.matches(
                    withEffectiveVisibility(
                        Visibility.VISIBLE
                    )
                )
            );
    }

//    @Test
//    fun test_autologin_token_expired() {
//        enqueueMockResponse()
//        `when`(preferences.loadRefreshToken()).thenReturn(REFRESH_TOKEN)
//
//        Thread.sleep(2000)
//
//        onView(withText(R.string.text_token_is_expired))
//            .check(
//                ViewAssertions.matches(
//                    withEffectiveVisibility(
//                        Visibility.VISIBLE
//                    )
//                )
//            );
//    }


}