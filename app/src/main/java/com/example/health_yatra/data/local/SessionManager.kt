package com.example.health_yatra.data.local

import android.content.Context

class SessionManager (context: Context){
    private val prefs = context.getSharedPreferences(
        "HealthYatraPrefs",
        Context.MODE_PRIVATE
    )

//    Save Token
fun saveToken(token: String, username: String){
    prefs.edit()
        .putString("jwt_token", token)
        .putString("username" , username)
        .apply()
}

    fun getToken(): String? {
        return prefs.getString("jwt_token", null)
    }

    fun isLoggedIn(): Boolean {
        return !getToken().isNullOrBlank()
    }

    fun clearSession() {
        prefs.edit().clear().apply()
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