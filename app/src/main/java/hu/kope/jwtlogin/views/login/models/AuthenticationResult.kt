package hu.kope.jwtlogin.views.login.models

data class AuthenticationResult(
    var authenticationError: Int? = null,
    var authenticationFinishedWithSuccess: Boolean = false
)