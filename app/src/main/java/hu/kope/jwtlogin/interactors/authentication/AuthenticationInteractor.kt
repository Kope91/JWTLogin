package hu.kope.jwtlogin.interactors.authentication

import hu.kope.jwtlogin.interactors.base.BaseInteractor
import hu.kope.jwtlogin.interactors.communication.IBackendService
import hu.kope.jwtlogin.interactors.communication.exceptions.RefreshTokenExpired
import hu.kope.jwtlogin.interactors.communication.models.AuthenticationModel
import hu.kope.jwtlogin.interactors.jwt.IJWTInteractor
import hu.kope.jwtlogin.interactors.users.IUserInteractor
import hu.kope.jwtlogin.views.login.models.LoginCredential
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AuthenticationInteractor : BaseInteractor(), IAuthenticationInteractor, KoinComponent {

    private val backendService: IBackendService by inject()
    private val JWTInteractor: IJWTInteractor by inject()
    private val userInteractor: IUserInteractor by inject()

    override suspend fun loginWithUserNameAndPassword(loginCredential: LoginCredential) {
        val authenticationModelResult = backendService.loginWithUserNameAndPassword(
            loginCredential.userName,
            loginCredential.password
        )
        handlingResponse(authenticationModelResult)
    }

    override suspend fun loginWithRefreshToken(refreshToken: String) {
        val authenticationModelResult: AuthenticationModel
        try {
            authenticationModelResult = backendService.loginWithRefreshToken(refreshToken)
        } catch (e: RefreshTokenExpired) {
            deleteUserData()
            throw e
        }
        handlingResponse(authenticationModelResult)
    }

    override fun deleteUserData() {
        userInteractor.deleteUserById(preferences.loadUserId())
        preferences.deleteUserId()
        preferences.deleteRefreshToken()
    }

    private fun handlingResponse(authenticationModelResutl: AuthenticationModel?) {
        authenticationModelResutl?.let {
            preferences.saveRefreshToken(it.refresh_token)
            saveUserModel(it.access_token)
        }
    }

    private fun saveUserModel(accessToken: String) {
        val userModel = JWTInteractor.decodeJWT(accessToken)
        preferences.saveUserId(userModel.userId)
        userInteractor.saveUserModel(userModel)
    }

}