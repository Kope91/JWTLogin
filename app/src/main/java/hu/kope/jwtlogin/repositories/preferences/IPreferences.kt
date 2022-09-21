package hu.kope.jwtlogin.repositories.preferences

interface IPreferences {

    fun saveRefreshToken(refreshToken: String)

    fun loadRefreshToken(): String

    fun deleteRefreshToken()

    fun saveUserId(userId : String)

    fun loadUserId() : String

    fun deleteUserId()

    fun saveClientId(clientId : String)

    fun loadClientId(): String

}