package com.example.health_yatra

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.health_yatra.fragment.DoctorDashboard
import com.example.health_yatra.fragment.Login
import com.example.health_yatra.fragment.SignUp

class DoctorViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, true)

        WindowInsetsControllerCompat(window, window.decorView).apply {
            isAppearanceLightStatusBars = true
        }

        setContentView(R.layout.activity_doctor_view)


        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.authFragmentContainer,
                    DoctorDashboard()
                )
                .commit()
        }

            supportFragmentManager.addOnBackStackChangedListener {

                val currentFragment =
                    supportFragmentManager.findFragmentById(
                        R.id.authFragmentContainer
                    )

                supportActionBar?.title = when (currentFragment) {
                    is SignUp -> "Sign Up"
                    is Login -> "Login"

                    else -> {
                        "Welcome !"
                    }
                }
            }
        }
    }
