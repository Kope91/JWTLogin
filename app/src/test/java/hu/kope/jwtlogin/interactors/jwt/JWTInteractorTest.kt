package hu.kope.jwtlogin.interactors.jwt

import hu.kope.jwtlogin.models.UserModel
import hu.kope.jwtlogin.models.UserRoleEnum
import org.junit.Assert
import org.junit.BeforeClass
import org.junit.Test
import java.util.*

class JWTInteractorTest {

    companion object {

        private val USER_ID = UUID.randomUUID().toString()
        private val USER_NAME = "Kope91"
        private val FULL_NAME = "Givenname Familyname"
        private val ROLE = UserRoleEnum.EDITOR


        private lateinit var JWTInteractor: IJWTInteractor

        val userModel =
            UserModel(USER_ID, USER_NAME, FULL_NAME, ROLE)

        @BeforeClass
        @JvmStatic
        fun setup() {
            JWTInteractor = JWTInteractor()
        }

    }

    @Test
    fun test_create_refhresToken() {
        val refreshToken = JWTInteractor.createRefreshToken()
        Assert.assertNotNull(refreshToken)
    }

    @Test
    fun test_create_JWTToken() {
        val JWTToken = JWTInteractor.createJWT(userModel, 120)
        Assert.assertNotNull(JWTToken)
    }

    @Test
    fun test_decode_JWTToken() {
        val JWTToken = JWTInteractor.createJWT(userModel, 120)

        val decodedUserModel = JWTInteractor.decodeJWT(JWTToken)

        Assert.assertEquals(userModel, decodedUserModel)
    }

    @Test
    fun test_JWTToken_is_expired() {
        val JWTToken = JWTInteractor.createJWT(userModel, 1)

        Thread.sleep(5000)

        Assert.assertTrue(JWTInteractor.JWTTokenIsExpired(JWTToken))
    }


}