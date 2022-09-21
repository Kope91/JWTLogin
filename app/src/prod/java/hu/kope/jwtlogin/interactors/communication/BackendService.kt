package hu.kope.jwtlogin.interactors.communication

import com.fasterxml.jackson.databind.ObjectMapper
import hu.kope.jwtlogin.interactors.communication.exceptions.InvalidCredentialsException
import hu.kope.jwtlogin.interactors.communication.exceptions.RefreshTokenExpired
import hu.kope.jwtlogin.interactors.communication.exceptions.UnexpectedError
import hu.kope.jwtlogin.interactors.communication.models.AuthenticationModel
import hu.kope.jwtlogin.repositories.preferences.IPreferences
import okhttp3.OkHttpClient
import okhttp3.internal.http.HTTP_OK
import okhttp3.internal.http.HTTP_UNAUTHORIZED
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

class BackendService : IBackendService, KoinComponent {

    private val service: IRestService by lazy { createRetrofitService() }
    private val objectMapper: ObjectMapper by inject()
    private val preferences: IPreferences by inject()

    companion object {
        const val BASE_URL = "https://example.vividmindsoft.com"
    }

    private fun createRetrofitService(): IRestService {

        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()


        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(JacksonConverterFactory.create(objectMapper))
            .client(client)
            .build()
        return retrofit.create(IRestService::class.java)

    }


    override fun loginWithUserNameAndPassword(
        userName: String,
        password: String
    ): AuthenticationModel {

        try {
            val response: Response<AuthenticationModel> = service.loginWithUserNameAndPassword(
                userName,
                password,
                IRestService.GRANT_TYPE.PASSWORD,
                preferences.loadClientId()
            ).execute()

            return checkResponse(response, true)
        } catch (e: Exception) {
            throw UnexpectedError()
        }


    }


    override fun loginWithRefreshToken(refreshToken: String): AuthenticationModel {


        try {
            val response: Response<AuthenticationModel> = service.loginWithRefreshToken(
                refreshToken,
                IRestService.GRANT_TYPE.REFRESH_TOKEN,
                preferences.loadClientId()
            ).execute()

            return checkResponse(response, false)
        } catch (e: Exception) {
            throw UnexpectedError()
        }


    }

    private fun checkResponse(
        response: Response<AuthenticationModel>,
        invalidCredential: Boolean
    ): AuthenticationModel {

        if (response.code() == HTTP_OK) {
            return response.body()!!
        } else if (response.code() == HTTP_UNAUTHORIZED) {
            if (invalidCredential) {
                throw InvalidCredentialsException()
            } else {
                throw RefreshTokenExpired()
            }
        } else {
            throw UnexpectedError()
        }

    }

}