package hu.kope.jwtlogin.interactors.jwt

import hu.kope.jwtlogin.views.login.models.LoginCredential
import org.junit.Assert
import org.junit.Test

class LoginCredentialTest {

    @Test
    fun test_empty_username_not_valid_username() {
        val loginCredential = LoginCredential("", "")

        Assert.assertFalse(loginCredential.isUserNameValid())
    }

    @Test
    fun test_empty_password_not_valid_password() {
        val loginCredential = LoginCredential("", "")

        Assert.assertFalse(loginCredential.isPasswordValid())
    }

    @Test
    fun test_filled_username_valid_username() {
        val loginCredential = LoginCredential("userName", "")

        Assert.assertTrue(loginCredential.isUserNameValid())
    }

    @Test
    fun test_filled_username_valid_password() {
        val loginCredential = LoginCredential("", "password")

        Assert.assertTrue(loginCredential.isPasswordValid())
    }

}