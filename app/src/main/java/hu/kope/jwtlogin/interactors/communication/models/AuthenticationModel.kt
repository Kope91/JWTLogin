package hu.kope.jwtlogin.interactors.communication.models

data class AuthenticationModel(
    val access_token: String,
    val token_type: String,
    val expires_in: Long,
    val refresh_token: String
)
