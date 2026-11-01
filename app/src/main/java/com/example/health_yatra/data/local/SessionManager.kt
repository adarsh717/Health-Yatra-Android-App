
package com.example.health_yatra.data.local

import android.content.Context

class SessionManager(context: Context) {

    private val prefs = context.getSharedPreferences(
        "HealthYatraPrefs",
        Context.MODE_PRIVATE
    )

    companion object {
        // Temporary session: only current app process
        private var temporaryToken: String? = null
        private var temporaryUsername: String? = null
    }

    // Save persistent token (Remember Me checked)
    fun saveToken(token: String, username: String) {
        temporaryToken = null
        temporaryUsername = null

        prefs.edit()
            .putString("jwt_token", token)
            .putString("username", username)
            .apply()
    }

    // Save temporary token - Remember Me unchecked
    fun saveTemporaryToken(token: String, username: String) {
        prefs.edit()
            .remove("jwt_token")
            .remove("username")
            .apply()

        temporaryToken = token
        temporaryUsername = username
    }

    // Get token from temporary or persistent session
    fun getToken(): String? {
        return temporaryToken
            ?: prefs.getString("jwt_token", null)
    }

    fun isLoggedIn(): Boolean {
        return !getToken().isNullOrBlank()
    }

    fun getUsername(): String? {
        return temporaryUsername
            ?: prefs.getString("username", null)
    }

    // Clear both types of session
    fun clearToken() {
        temporaryToken = null
        temporaryUsername = null

        prefs.edit()
            .remove("jwt_token")
            .remove("username")
            .apply()
    }

    fun clearSession() {
        clearToken()
        prefs.edit()
            .clear()
            .apply()
    }

    fun savePatientDetails(name: String, aadhaar: String) {
        prefs.edit()
            .putString("patient_name", name)
            .putString("patient_aadhaar", aadhaar)
            .apply()
    }

    fun getPatientName(): String? {
        return prefs.getString("patient_name", null)
    }

    fun getPatientAadhaar(): String? {
        return prefs.getString("patient_aadhaar", null)
    }
}
