package hu.kope.jwtlogin.views.login.models

class LoginCredential(val userName: String, val password: String) {

    fun isUserNameValid(): Boolean {
        return userName.isNotBlank()
    }

    fun isPasswordValid(): Boolean {
        return password.isNotBlank()
    }

}