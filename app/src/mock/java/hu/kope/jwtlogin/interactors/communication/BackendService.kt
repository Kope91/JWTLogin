package hu.kope.jwtlogin.interactors.communication

import hu.kope.jwtlogin.interactors.communication.exceptions.InvalidCredentialsException
import hu.kope.jwtlogin.interactors.communication.exceptions.RefreshTokenExpired
import hu.kope.jwtlogin.interactors.communication.exceptions.UnexpectedError
import hu.kope.jwtlogin.interactors.communication.models.AuthenticationModel
import hu.kope.jwtlogin.interactors.jwt.IJWTInteractor
import hu.kope.jwtlogin.interactors.jwt.JWTInteractor.Companion.TOKEN_EXPIRATION_IN_SECONDS
import hu.kope.jwtlogin.interactors.users.IUserInteractor
import hu.kope.jwtlogin.models.UserModel
import hu.kope.jwtlogin.models.UserRoleEnum
import hu.kope.jwtlogin.repositories.preferences.IPreferences
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*

class BackendService : IBackendService, KoinComponent {

    private val JWTInteractor: IJWTInteractor by inject()
    private val preferences: IPreferences by inject()
    private val userInteractor: IUserInteractor by inject()


    override fun loginWithUserNameAndPassword(
        userName: String,
        password: String
    ): AuthenticationModel {
        if (userName.lowercase() == INVALID || password.lowercase() == INVALID) {
            throw InvalidCredentialsException()
        } else if (userName.lowercase() == ERROR || password.lowercase() == ERROR) {
            throw UnexpectedError()
        } else {

            val JWTToken = createJWTToken(userName)

            val refreshToken = JWTInteractor.createRefreshToken()

            return AuthenticationModel(
                access_token = JWTToken,
                token_type = TOKEN_TYPE,
                expires_in = TOKEN_EXPIRATION_IN_SECONDS,
                refresh_token = refreshToken
            )

        }
    }

    private fun createJWTToken(userName: String): String {
        return JWTInteractor.createJWT(
            UserModel(
                UUID.randomUUID().toString(),
                userName,
                FULL_NAME,
                UserRoleEnum.EDITOR
            ), expiresInSeconds = TOKEN_EXPIRATION_IN_SECONDS
        )
    }

    override fun loginWithRefreshToken(refreshToken: String): AuthenticationModel {
        if (JWTInteractor.JWTTokenIsExpired(refreshToken)) {
            throw RefreshTokenExpired()
        } else {

            val userModel = userInteractor.getUserById(preferences.loadUserId())

            val JWTToken =
                JWTInteractor.createJWT(
                    userModel,
                    expiresInSeconds = TOKEN_EXPIRATION_IN_SECONDS
                )

            val newRefreshToken = JWTInteractor.createRefreshToken()

            return AuthenticationModel(
                access_token = JWTToken,
                token_type = TOKEN_TYPE,
                expires_in = TOKEN_EXPIRATION_IN_SECONDS,
                refresh_token = newRefreshToken
            )

        }
    }

    companion object {
        private const val ERROR = "error"
        private const val INVALID = "invalid"
        private const val FULL_NAME = "Givenname Familyname"
        private const val TOKEN_TYPE = "bearer"
    }
}