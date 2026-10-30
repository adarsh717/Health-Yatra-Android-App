package com.example.health_yatra.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.health_yatra.R
import com.example.health_yatra.data.model.RegisterRequest
import com.example.health_yatra.data.remote.RetrofitClient
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import kotlinx.coroutines.launch

class SignUp : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(
            R.layout.fragment_sign_up,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRoleDropdown(view)
        setupEmailVerificationUI(view)
        setupRegister(view)
        setupLoginNavigation(view)
    }

    // Role Dropdown
    private fun setupRoleDropdown(view: View) {

        val roles = arrayOf("Patient", "Doctor")

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            roles
        )

        val role = view.findViewById<MaterialAutoCompleteTextView>(R.id.role)

        role.setAdapter(adapter)
    }

    // Temporary OTP UI
    private fun setupEmailVerificationUI(view: View) {
        val verifyBtn = view.findViewById<Button>(R.id.btnVerifyEmail)
        val otpFrame = view.findViewById<FrameLayout>(R.id.otpFrame)
        verifyBtn.setOnClickListener {
            otpFrame.visibility = View.VISIBLE
        }
    }

    // Register API
    private fun setupRegister(view: View) {

        val email = view.findViewById<AppCompatEditText>(R.id.email)
        val username = view.findViewById<AppCompatEditText>(R.id.username)
        val phone = view.findViewById<AppCompatEditText>(R.id.phoneNumber)
        val aadhaar = view.findViewById<AppCompatEditText>(R.id.Aadhaar)
        val password = view.findViewById<AppCompatEditText>(R.id.password)
        val role = view.findViewById<MaterialAutoCompleteTextView>(R.id.role)
        val registerBtn = view.findViewById<MaterialButton>(R.id.registerBtn)

        registerBtn.setOnClickListener {
            // Read form values
            val emailValue = email.text.toString().trim()
            val usernameValue = username.text.toString().trim()
            val phoneValue = phone.text.toString().trim()
            val aadhaarValue = aadhaar.text.toString().trim()
            val passwordValue = password.text.toString()
            val roleValue = role.text.toString().trim()

            // Empty validation
            if (
                emailValue.isEmpty() ||
                usernameValue.isEmpty() ||
                phoneValue.isEmpty() ||
                aadhaarValue.isEmpty() ||
                passwordValue.isEmpty() ||
                roleValue.isEmpty()
            ) {
                Toast.makeText(
                    requireContext(),
                    "Please fill all fields",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            // Email validation
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailValue).matches()) {
                email.error = "Enter a valid email"
                return@setOnClickListener
            }
            // Phone validation
            if (phoneValue.length != 10) {
                phone.error = "Enter valid 10 digit phone number"
                return@setOnClickListener
            }
            // Aadhaar validation
            if (aadhaarValue.length != 12) {
                aadhaar.error = "Enter valid 12 digit Aadhaar number"
                return@setOnClickListener
            }
            // Convert role
            val backendRole = when (roleValue) {
                "Patient" -> "USER"
                "Doctor" -> "DOCTOR"
                else -> null
            }

            if (backendRole == null) {
                Toast.makeText(
                    requireContext(),
                    "Please select a valid role",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            // Disable button during API call
            registerBtn.isEnabled = false

            // API call
            viewLifecycleOwner.lifecycleScope.launch {

                try {

                    val request = RegisterRequest(
                        username = usernameValue,
                        name = emailValue,
                        phone = phoneValue,
                        aadhar = aadhaarValue,
                        password = passwordValue,
                        role = backendRole
                    )

                    val response = RetrofitClient.apiService.register(request)

                    if (response.isSuccessful) {

                        Toast.makeText(
                            requireContext(),
                            response.body() ?: "Registration successful",
                            Toast.LENGTH_LONG
                        ).show()



                    } else {

                        Toast.makeText(
                            requireContext(),
                            "Registration failed: HTTP ${response.code()}",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                } catch (e: Exception) {

                    Toast.makeText(
                        requireContext(),
                        "Error: ${e.localizedMessage}",
                        Toast.LENGTH_LONG
                    ).show()

                } finally {

                    registerBtn.isEnabled = true
                }
            }
        }
    }

    // Login Navigation
    private fun setupLoginNavigation(view: View) {

        val tvLogin = view.findViewById<TextView>(R.id.tvLogin)

        tvLogin.setOnClickListener {

            parentFragmentManager.beginTransaction()
                
                .replace(
                    R.id.authFragmentContainer,
                    Login()
                ).addToBackStack("SignUp").commit()

            (requireActivity() as? AppCompatActivity)
                ?.supportActionBar?.title = "Login"
        }
    }
}
