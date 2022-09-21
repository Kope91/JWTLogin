package hu.kope.jwtlogin.views.login.models

data class InputValidationResult(
    var usernameError: Int? = null,
    var passwordError: Int? = null,
    var isDataValid: Boolean = false
)