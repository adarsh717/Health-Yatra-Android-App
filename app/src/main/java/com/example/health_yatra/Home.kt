package com.example.health_yatra

import android.os.Bundle
import android.view.View
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

    private fun checkUserSession() {

        val savedToken = sessionManager.getToken()

        // No saved token: open Login
        if (savedToken.isNullOrBlank()) {
            openLogin()
            return
        }

        // validate with backend
        lifecycleScope.launch {

            try {

                val response = RetrofitClient.apiService.getLoggedInUser(
                        "Bearer $savedToken"
                    )
                if (response.isSuccessful && !response.body().isNullOrBlank()
                ) {
                    // Token is valid
                    openPatientDashboard()
                } else if (
                    response.code() == 401 ||
                    response.code() == 403
                ) {
                    // Token is invalid or expired
                    sessionManager.clearSession()
                    openLogin()
                } else {

                    // Other server error
                    Toast.makeText(
                        this@Home,
                        "Unable to verify session: HTTP ${response.code()}",
                        Toast.LENGTH_LONG
                    ).show()
                }

            } catch (e: Exception) {

                // Network/server connection problem
                Toast.makeText(
                    this@Home,
                    "Connection error: ${e.localizedMessage}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun openLogin() {

        supportActionBar?.title = "Login"

        supportFragmentManager.beginTransaction()
            .replace(
                R.id.authFragmentContainer,
                Login()
            )
            .commit()
    }

    private fun openPatientDashboard() {
        supportActionBar?.title = "Patient Dashboard"
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.authFragmentContainer,
                PatientDashBoard()
            )
            .commit()
    }


}
