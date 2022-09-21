package hu.kope.jwtlogin.interactors.communication

import hu.kope.jwtlogin.interactors.communication.models.AuthenticationModel

interface IBackendService {

    fun loginWithUserNameAndPassword(userName: String, password: String): AuthenticationModel

    fun loginWithRefreshToken(refreshToken: String): AuthenticationModel
}