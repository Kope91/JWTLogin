package hu.kope.jwtlogin.views.home

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.fasterxml.jackson.databind.ObjectMapper
import hu.kope.jwtlogin.R
import hu.kope.jwtlogin.interactors.communication.IRestService
import hu.kope.jwtlogin.interactors.communication.models.AuthenticationModel
import hu.kope.jwtlogin.interactors.jwt.IJWTInteractor
import hu.kope.jwtlogin.interactors.jwt.JWTInteractor
import hu.kope.jwtlogin.models.UserModel
import hu.kope.jwtlogin.models.UserRoleEnum
import hu.kope.jwtlogin.views.login.LoginActivity
import okhttp3.internal.http.HTTP_OK
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit


@RunWith(AndroidJUnit4ClassRunner::class)
class HomeActivityTest {


    private lateinit var server: MockWebServer
    private lateinit var backendService: IRestService
    private lateinit var jwtInteractor: IJWTInteractor

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


        jwtInteractor = JWTInteractor()

        val activityScenario = ActivityScenario.launch(LoginActivity::class.java)
    }

    private fun enqueueMockResponse() {
        val mockResponse = MockResponse()
        mockResponse.http2ErrorCode = HTTP_OK
        mockResponse.setBody(createBody())
        mockResponse.setBodyDelay(2, TimeUnit.SECONDS)
        server.enqueue(mockResponse)
    }


    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun test_login_user_with_editor_credential() {


        Espresso.onView(ViewMatchers.withId(R.id.username))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.username))
            .perform(ViewActions.replaceText(USER_NAME))
        Espresso.onView(ViewMatchers.withId(R.id.password))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.password))
            .perform(ViewActions.replaceText(PASSWORD))
        Espresso.onView(ViewMatchers.withId(R.id.login))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.login))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed())).perform().perform(
                ViewActions.click()
            )
        enqueueMockResponse()

        Thread.sleep(5000)


        Espresso.onView(ViewMatchers.withId(R.id.username))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.username))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            .check(ViewAssertions.matches(ViewMatchers.withText(FULL_NAME)))

        Espresso.onView(ViewMatchers.withId(R.id.role))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.role))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            .check(ViewAssertions.matches(ViewMatchers.withText(EDITOR)))
    }


    private fun createJWTToken(userName: String): String {
        return jwtInteractor.createJWT(
            UserModel(
                UUID.randomUUID().toString(),
                userName,
                FULL_NAME,
                UserRoleEnum.EDITOR
            ), expiresInSeconds = JWTInteractor.TOKEN_EXPIRATION_IN_SECONDS
        )
    }

    private fun createBody(): String {
        val objectMapper = ObjectMapper()
        val JWTToken = createJWTToken(USER_NAME)
        val refreshToken = jwtInteractor.createRefreshToken()

        val authenticationModel = AuthenticationModel(
            access_token = JWTToken,
            token_type = BEARER,
            expires_in = JWTInteractor.TOKEN_EXPIRATION_IN_SECONDS,
            refresh_token = refreshToken
        )

        return objectMapper.writeValueAsString(authenticationModel)
    }

    companion object {
        private const val USER_NAME = "Kope91"
        private const val BEARER = "bearer"
        private const val FULL_NAME = "Givenname Familyname"
        private const val EDITOR = "Editor"
        private const val PASSWORD = "password"
    }


}