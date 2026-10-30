package com.example.health_yatra.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.health_yatra.R
import com.example.health_yatra.data.model.LoginRequest
import com.example.health_yatra.data.remote.RetrofitClient
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch
import com.example.health_yatra.data.local.SessionManager

class Login : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return inflater.inflate(
            R.layout.fragment_login,
            container,
            false
        )
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        setupLogin(view)
    }

    private fun setupLogin(view: View) {

        val username = view.findViewById<EditText>(R.id.LoginUsername)
        val password = view.findViewById<EditText>(R.id.LoginPassword)
        val loginBtn = view.findViewById<MaterialButton>(R.id.LoginBtn)

        loginBtn.setOnClickListener {

            val email = username.text.toString().trim()
            val pass = password.text.toString()

            if (email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Please enter email and password",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            loginBtn.isEnabled = false

            viewLifecycleOwner.lifecycleScope.launch {
                try {
                    val response =
                        RetrofitClient.apiService.login(
                            LoginRequest(
                                username = email,
                                password = pass
                            )
                        )

                    if (response.isSuccessful) {

                        val loginResponse = response.body()

                        if (loginResponse != null) {

//                            requireContext()
//                                .getSharedPreferences("HealthYatraPrefs", Context.MODE_PRIVATE)
//                                .edit {
//                                         putString("jwt_token", loginResponse.token)
//                                        .putString("username", loginResponse.username)
//                                }
                            val sessionManager = SessionManager(requireContext())

                            sessionManager.saveToken(
                                loginResponse.token,
                                loginResponse.username
                            )

                            Toast.makeText(
                                requireContext(),
                                "Login successful",
                                Toast.LENGTH_SHORT
                            ).show()

                            parentFragmentManager.beginTransaction()
                                .replace(
                                    R.id.authFragmentContainer,
                                    PatientDashBoard()
                                ).commit()
                        }

                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Login failed: ${response.code()}",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                } catch (e: Exception) {
                    Toast.makeText(
                        requireContext(),
                        "Network error: ${e.localizedMessage}",
                        Toast.LENGTH_LONG
                    ).show()

                } finally {
                    loginBtn.isEnabled = true
                }
            }
        }
    }
}
