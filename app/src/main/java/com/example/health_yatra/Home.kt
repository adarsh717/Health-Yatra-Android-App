package com.example.health_yatra

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import com.example.health_yatra.data.local.SessionManager
import com.example.health_yatra.data.remote.RetrofitClient
import com.example.health_yatra.fragment.Login
import com.example.health_yatra.fragment.PatientDashBoard
import com.example.health_yatra.fragment.SignUp
import kotlinx.coroutines.launch

class Home : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, true)

        WindowInsetsControllerCompat(window, window.decorView).apply {
            isAppearanceLightStatusBars = true
        }

        setContentView(R.layout.activity_home)

        sessionManager = SessionManager(this)

        supportFragmentManager.addOnBackStackChangedListener {
            val currentFragment =
                supportFragmentManager.findFragmentById(
                    R.id.authFragmentContainer
                )

            supportActionBar?.title = when (currentFragment) {
                is SignUp -> "Sign Up"
                is Login -> "Login"
                is PatientDashBoard -> "Patient Dashboard"
                else -> "Health-Yatra"
            }
        }

        if (savedInstanceState == null) {
            checkUserSession()
        }
    }

    // Check session and navigate according to backend role
    fun checkUserSession() {

        val savedToken = sessionManager.getToken()

        if (savedToken.isNullOrBlank()) {
            openLogin()
            return
        }

        lifecycleScope.launch {

            try {
                // STEP 1: Get logged-in user's Aadhaar using token
                val response = RetrofitClient.apiService
                    .getLoggedInUser("Bearer $savedToken")

                if (response.code() == 401 || response.code() == 403) {
                    sessionManager.clearSession()
                    openLogin()
                    return@launch
                }

                if (!response.isSuccessful) {
                    Log.e(
                        "ROLE_CHECK",
                        "LoggedIn API failed: ${response.code()}"
                    )
                    showMessage("Unable to verify login session")
                    return@launch
                }

                val aadhaar = response.body()?.trim()

                if (aadhaar.isNullOrBlank()) {
                    Log.e("ROLE_CHECK", "Aadhaar is empty")
                    showMessage("Aadhaar not received from server")
                    return@launch
                }

                Log.d("ROLE_CHECK", "Aadhaar received successfully")

                // STEP 2: Fetch registration details using Aadhaar
                val roleResponse = RetrofitClient.apiService
                    .getUserRole(aadhaar)

                if (!roleResponse.isSuccessful) {
                    Log.e(
                        "ROLE_CHECK",
                        "Role API failed: ${roleResponse.code()} - " +
                                "${roleResponse.errorBody()?.string()}"
                    )
                    showMessage("Unable to fetch user role")
                    return@launch
                }

                // STEP 3: Extract role from response
                val user = roleResponse.body()

                if (user == null) {
                    Log.e("ROLE_CHECK", "User response is null")
                    showMessage("User details not received")
                    return@launch
                }

                val role = user.role.trim().uppercase()

                Log.d("ROLE_CHECK", "Backend role received: $role")

                // STEP 4: Navigate according to role
                when (role) {

                    "DOCTOR" -> {
                        openDoctorDashboard()
                    }

                    "USER", "PATIENT" -> {
                        openPatientDashboard()
                    }

                    "ADMIN" -> {
                        showMessage("Admin dashboard not implemented")
                    }

                    else -> {
                        Log.e("ROLE_CHECK", "Unknown role: $role")
                        showMessage("Unknown user role: $role")
                    }
                }

            } catch (e: Exception) {

                Log.e("ROLE_CHECK", "Role checking failed", e)

                showMessage(
                    "Connection error: ${e.localizedMessage}"
                )
            }
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(
            this,
            message,
            Toast.LENGTH_LONG
        ).show()
    }

    // Open Login Fragment
    private fun openLogin() {

        supportActionBar?.title = "Login"

        supportFragmentManager.beginTransaction()
            .replace(
                R.id.authFragmentContainer,
                Login()
            )
            .commit()
    }

    // Open Patient Dashboard Fragment
    private fun openPatientDashboard() {

        supportActionBar?.title = "Patient Dashboard"

        supportFragmentManager.beginTransaction()
            .replace(
                R.id.authFragmentContainer,
                PatientDashBoard()
            )
            .commit()
    }

    // Open Doctor Dashboard Activity
    private fun openDoctorDashboard() {

        val intent = Intent(
            this,
            DoctorViewActivity::class.java
        )
        startActivity(intent)
        finish()
    }
}
