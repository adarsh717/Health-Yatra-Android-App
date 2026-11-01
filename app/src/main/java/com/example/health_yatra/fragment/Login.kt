
package com.example.health_yatra.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.radiobutton.MaterialRadioButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.health_yatra.R
import com.example.health_yatra.data.model.LoginRequest
import com.example.health_yatra.data.model.CheckUserRequest
import com.example.health_yatra.data.remote.RetrofitClient
import com.example.health_yatra.data.local.SessionManager
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch

class Login : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(
            R.layout.fragment_login,
            container,
            false
        )
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        setupLogin(view)

        val registerFrag =
            view.findViewById<TextView>(R.id.tvRegister)

        registerFrag.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.authFragmentContainer, SignUp())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun setupLogin(view: View) {

        val username = view.findViewById<EditText>(R.id.LoginUsername)

        val password = view.findViewById<EditText>(R.id.LoginPassword)

        val loginBtn = view.findViewById<MaterialButton>(R.id.LoginBtn)

        val rememberMe = view.findViewById<MaterialRadioButton>(R.id.rememberBtn)

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

                    // Check whether user exists

                    val checkResponse =
                        RetrofitClient.apiService.checkUser(
                            CheckUserRequest(email)
                        )

                    if (!checkResponse.isSuccessful) {
                        Toast.makeText(
                            requireContext(),
                            "User check failed: ${checkResponse.code()}",
                            Toast.LENGTH_LONG
                        ).show()

                        return@launch
                    }

                    val isRegistered = checkResponse.body()

                    if (isRegistered == null) {
                        Toast.makeText(
                            requireContext(),
                            "Invalid server response",
                            Toast.LENGTH_LONG
                        ).show()

                        return@launch
                    }

                    if (!isRegistered) {
                        Toast.makeText(
                            requireContext(),
                            "User not registered. Please Sign Up.",
                            Toast.LENGTH_LONG
                        ).show()

                        parentFragmentManager.beginTransaction()
                            .replace(
                                R.id.authFragmentContainer,
                                SignUp()
                            )
                            .addToBackStack(null)
                            .commit()

                        return@launch
                    }

                    // User exists, call Login API

                    val response = RetrofitClient.apiService.login(
                            LoginRequest(
                                username = email,
                                password = pass
                            )
                        )

                    if (response.isSuccessful) {

                        val loginResponse = response.body()

                        if (loginResponse != null) {
                            val sessionManager = SessionManager(requireContext())

                            //Remember Me functionality
                            if (rememberMe.isChecked) {

                                sessionManager.saveToken(
                                    loginResponse.token,
                                    loginResponse.username
                                )
                            }else {

                                sessionManager.saveTemporaryToken(
                                    loginResponse.token,
                                    loginResponse.username
                                )
                            }

                            Toast.makeText(
                                requireContext(),
                                "Login successful",
                                Toast.LENGTH_SHORT
                            ).show()

                            // Open Patient Dashboard

                            parentFragmentManager.beginTransaction()
                                .replace(
                                    R.id.authFragmentContainer,
                                    PatientDashBoard()
                                )
                                .commit()
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
