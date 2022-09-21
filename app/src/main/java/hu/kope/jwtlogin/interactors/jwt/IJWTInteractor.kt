package hu.kope.jwtlogin.interactors.jwt

import hu.kope.jwtlogin.models.UserModel

interface IJWTInteractor {

    fun createRefreshToken() : String

    fun createJWT(userModel: UserModel, expiresInSeconds : Long) : String

    fun decodeJWT(JWTToken : String) : UserModel

    fun JWTTokenIsExpired(JWTToken : String) : Boolean

}