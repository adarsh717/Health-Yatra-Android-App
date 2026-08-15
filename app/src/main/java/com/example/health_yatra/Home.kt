package com.example.health_yatra

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.health_yatra.fragment.Login
import com.example.health_yatra.fragment.SignUp
import com.google.android.material.appbar.MaterialToolbar

class Home : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, true)

        WindowInsetsControllerCompat(window, window.decorView).apply {
            isAppearanceLightStatusBars = true
        }

        setContentView(R.layout.activity_home)

//        val toolbar = findViewById<MaterialToolbar>(R.id.topBar)
//        setSupportActionBar(toolbar)

        supportActionBar?.title = "Sign Up"

        if (savedInstanceState == null) {

            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.authFragmentContainer,
                    SignUp()
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
                else -> "Health-Yatra"
            }
        }
    }

    // Toolbar back arrow
    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}