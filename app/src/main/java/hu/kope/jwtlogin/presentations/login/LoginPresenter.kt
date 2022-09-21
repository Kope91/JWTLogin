package hu.kope.jwtlogin.presentations.login

import hu.kope.jwtlogin.R
import hu.kope.jwtlogin.interactors.authentication.IAuthenticationInteractor
import hu.kope.jwtlogin.interactors.communication.exceptions.ConnectionException
import hu.kope.jwtlogin.interactors.communication.exceptions.InvalidCredentialsException
import hu.kope.jwtlogin.interactors.communication.exceptions.RefreshTokenExpired
import hu.kope.jwtlogin.interactors.communication.exceptions.UnexpectedError
import hu.kope.jwtlogin.presentations.base.BasePresenter
import hu.kope.jwtlogin.views.login.ILoginActivity
import hu.kope.jwtlogin.views.login.models.AuthenticationResult
import hu.kope.jwtlogin.views.login.models.InputValidationResult
import hu.kope.jwtlogin.views.login.models.LoginCredential
import org.koin.core.component.inject
import java.util.*

class LoginPresenter : BasePresenter(), ILoginPresenter {

    private val authenticationInteractor: IAuthenticationInteractor by inject()

    override fun validateUserNameAndPassword(loginCredential: LoginCredential) {

        val inputValidationResult = InputValidationResult()

        if (!loginCredential.isUserNameValid()) {
            inputValidationResult.apply {
                usernameError = R.string.text_missing_username
            }
        } else if (!loginCredential.isPasswordValid()) {
            inputValidationResult.apply {
                passwordError = R.string.text_missing_password
            }
        } else {
            inputValidationResult.apply {
                isDataValid = true
            }
        }

        (baseActivity as? ILoginActivity)?.showInputValidationResult(inputValidationResult)
    }

    override fun generateClientId() {
        if (preferences.loadClientId().isBlank()) {
            preferences.saveClientId(UUID.randomUUID().toString())
        }
    }

    override suspend fun loginWithUserNameAndPassword(
        loginCredential: LoginCredential
    ): AuthenticationResult {
        val authenticationResult = AuthenticationResult()

        try {
            authenticationInteractor.loginWithUserNameAndPassword(
                loginCredential
            )
            authenticationResult.authenticationFinishedWithSuccess = true
        } catch (e: InvalidCredentialsException) {
            authenticationResult.authenticationError = R.string.text_invalid_username_or_password
        } catch (e: ConnectionException) {
            authenticationResult.authenticationError = R.string.text_connection_error
        } catch (e: UnexpectedError) {
            authenticationResult.authenticationError = R.string.text_unexpected_error
        }

        return authenticationResult
    }


    override suspend fun loginWithRefreshToken(): AuthenticationResult {

        val authenticationResult = AuthenticationResult()

        if (preferences.loadRefreshToken().isBlank()) {
            authenticationResult.authenticationFinishedWithSuccess = true
            return authenticationResult
        }

        try {
            authenticationInteractor.loginWithRefreshToken(preferences.loadRefreshToken())
            authenticationResult.authenticationFinishedWithSuccess = true
        } catch (e: RefreshTokenExpired) {
            authenticationResult.authenticationError = R.string.text_token_is_expired
        } catch (e: ConnectionException) {
            authenticationResult.authenticationError = R.string.text_connection_error
        } catch (e: UnexpectedError) {
            authenticationResult.authenticationError = R.string.text_unexpected_error
        }

        return authenticationResult
    }

    override fun hasRefreshToken(): Boolean {
        return preferences.loadRefreshToken().isNotBlank()
    }
}