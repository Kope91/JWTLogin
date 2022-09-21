package hu.kope.jwtlogin.presentations.login

import hu.kope.jwtlogin.presentations.base.IBasePresenter
import hu.kope.jwtlogin.views.login.models.AuthenticationResult
import hu.kope.jwtlogin.views.login.models.LoginCredential

interface ILoginPresenter : IBasePresenter {
    fun validateUserNameAndPassword(loginCredential: LoginCredential)
    fun generateClientId()
    fun hasRefreshToken(): Boolean
    suspend fun loginWithUserNameAndPassword(loginCredential: LoginCredential): AuthenticationResult
    suspend fun loginWithRefreshToken(): AuthenticationResult
}