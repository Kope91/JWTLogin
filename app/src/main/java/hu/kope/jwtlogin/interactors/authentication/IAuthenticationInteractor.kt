package hu.kope.jwtlogin.interactors.authentication

import hu.kope.jwtlogin.views.login.models.LoginCredential

interface IAuthenticationInteractor {

    suspend fun loginWithUserNameAndPassword(loginCredential: LoginCredential)

    suspend fun loginWithRefreshToken(refreshToken: String)
    fun deleteUserData()

}