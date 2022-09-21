package hu.kope.jwtlogin.repositories.preferences

import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase.assertEquals
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PreferencesTest {


    companion object {
        private val TEST_TOKEN = "test_refresh_token"
        private val TEST_USER_ID = "test_user_id"
        private val TEST_CLIENT_ID = "test_client_id"

        private lateinit var preferences: IPreferences

        @BeforeClass
        @JvmStatic
        fun setup() {
            preferences = Preferences()
        }

        @AfterClass
        @JvmStatic
        fun teardown() {
            preferences.deleteRefreshToken()
        }

    }


    @Before
    @Throws(Exception::class)
    fun setUp() {
        preferences.deleteRefreshToken()
    }

    @Test
    fun test_not_stored_jwt() {
        assertEquals("", preferences.loadRefreshToken())
    }

    @Test
    fun test_load_stored_jwt() {
        preferences.saveRefreshToken(TEST_TOKEN)
        assertEquals(TEST_TOKEN, preferences.loadRefreshToken())
    }

    @Test
    fun test_delete_stored_jwt_token() {
        preferences.saveRefreshToken(TEST_TOKEN)
        assertEquals(TEST_TOKEN, preferences.loadRefreshToken())
        preferences.deleteRefreshToken()
        assertEquals("", preferences.loadRefreshToken())
    }

    @Test
    fun test_not_stored_userid() {
        assertEquals("", preferences.loadUserId())
    }

    @Test
    fun test_load_stored_userid() {
        preferences.saveUserId(TEST_USER_ID)
        assertEquals(TEST_USER_ID, preferences.loadUserId())
    }

    @Test
    fun test_delete_stored_userid_token() {
        preferences.saveUserId(TEST_USER_ID)
        assertEquals(TEST_USER_ID, preferences.loadUserId())
        preferences.deleteUserId()
        assertEquals("", preferences.loadUserId())
    }

    @Test
    fun test_not_stored_clientid() {
        assertEquals("", preferences.loadClientId())
    }

    @Test
    fun test_load_stored_clientid() {
        preferences.saveUserId(TEST_CLIENT_ID)
        assertEquals(TEST_CLIENT_ID, preferences.loadClientId())
    }

}