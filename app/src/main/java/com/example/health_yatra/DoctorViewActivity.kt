package com.example.health_yatra

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import com.example.health_yatra.data.local.SessionManager
import com.example.health_yatra.data.remote.RetrofitClient
import com.example.health_yatra.fragment.DoctorPatientsFragment
import kotlinx.coroutines.launch

class DoctorViewActivity : AppCompatActivity() {

    private lateinit var drName: TextView
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, true)

        WindowInsetsControllerCompat(window, window.decorView).apply {
            isAppearanceLightStatusBars = true
        }

        setContentView(R.layout.activity_doctor_view)

        sessionManager = SessionManager(this)

        drName = findViewById(R.id.tvDoctorName)

        getDoctorDetails()

        // Load fragment only once
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.authFragmentContainer,
                    DoctorPatientsFragment()
                )
                .commit()
        }
    }

    private fun getDoctorDetails() {

        lifecycleScope.launch {
            try {
                val token = sessionManager.getToken()

                if (token.isNullOrBlank()) {
                    drName.text = "Welcome, Doctor"
                    return@launch
                }

                // Step 1: Get Aadhaar
                val response = RetrofitClient.apiService
                    .getLoggedInUser("Bearer $token")

                if (!response.isSuccessful) {
                    drName.text = "Welcome, Doctor"
                    return@launch
                }

                val aadhaar = response.body()?.trim()

                if (aadhaar.isNullOrBlank()) {
                    drName.text = "Welcome, Doctor"
                    return@launch
                }

                // Step 2: Get doctor details
                val roleResponse = RetrofitClient.apiService
                    .getUserRole(aadhaar)

                if (roleResponse.isSuccessful) {

                    val user = roleResponse.body()

                    if (user?.role?.equals(
                            "DOCTOR",
                            ignoreCase = true
                        ) == true
                    ) {
                        drName.text = "Welcome, Dr. Nigga"
                    } else {
                        drName.text = "Welcome, Doctor"
                    }

                } else {
                    drName.text = "Welcome, Doctor"

                    Toast.makeText(
                        this@DoctorViewActivity,
                        "Unable to fetch doctor details",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            } catch (e: Exception) {

                drName.text = "Welcome, Doctor"

                Toast.makeText(
                    this@DoctorViewActivity,
                    "Unable to load doctor details: ${e.localizedMessage}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}