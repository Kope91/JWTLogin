package hu.kope.jwtlogin.repositories.preferences

import android.content.Context
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class Preferences : IPreferences, KoinComponent {

    private val context: Context by inject()

    private var sharedPreferences: android.content.SharedPreferences =
        context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE)

    override fun saveRefreshToken(refreshToken: String) {
        saveString(REFRESH_TOKEN_KEY, refreshToken)
    }

    override fun loadRefreshToken(): String = loadString(REFRESH_TOKEN_KEY)

    override fun deleteRefreshToken() = deleteString(REFRESH_TOKEN_KEY)

    override fun saveUserId(userId: String) {
        saveString(USER_ID_KEY, userId)
    }

    override fun loadUserId(): String = loadString(USER_ID_KEY)

    override fun deleteUserId() = deleteString(USER_ID_KEY)

    override fun saveClientId(clientId: String) {
        saveString(CLIENT_ID_KEY, clientId)
    }

    override fun loadClientId(): String = loadString(CLIENT_ID_KEY)

    private fun loadString(key: String): String = sharedPreferences.getString(key, "")!!

    private fun deleteString(key: String) {
        val editor = sharedPreferences.edit()
        editor.remove(key)
        editor.apply()
    }

    private fun saveString(key: String, value: String) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    companion object {
        const val REFRESH_TOKEN_KEY = "refreshToken"
        const val USER_ID_KEY = "userId"
        const val CLIENT_ID_KEY = "clientId"

        const val PREF_FILE = "JWTLoginAppPref"
    }


}