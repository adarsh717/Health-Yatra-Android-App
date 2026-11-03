package com.example.health_yatra.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.example.health_yatra.model.VerifyEmailRequest
import com.example.health_yatra.data.remote.RetrofitClient
import com.google.android.material.button.MaterialButton
import com.google.android.material.checkbox.MaterialCheckBox
import kotlinx.coroutines.launch

class SignUp : Fragment() {

    // Email verification state
    private var isEmailVerified = false
    private var verifiedEmail: String? = null

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

        setupRoleSelection(view)
        setupEmailVerificationUI(view)
        setupRegister(view)
        setupLoginNavigation(view)
    }

    // Patient / Doctor checkbox selection
    private fun setupRoleSelection(view: View) {

        val patientCheckBox =
            view.findViewById<MaterialCheckBox>(R.id.patientCheckBox)

        val doctorCheckBox =
            view.findViewById<MaterialCheckBox>(R.id.doctorCheckBox)

        patientCheckBox.setOnCheckedChangeListener { _, isChecked ->

            if (isChecked) {
                doctorCheckBox.isChecked = false
            }
        }

        doctorCheckBox.setOnCheckedChangeListener { _, isChecked ->

            if (isChecked) {
                patientCheckBox.isChecked = false
            }
        }
    }

    // Email verification API
    private fun setupEmailVerificationUI(view: View) {

        val email =
            view.findViewById<AppCompatEditText>(R.id.email)

        val verifyBtn =
            view.findViewById<Button>(R.id.btnVerifyEmail)

        val otpFrame =
            view.findViewById<FrameLayout>(R.id.otpFrame)

        // Reset verification if email changes
        email.setOnFocusChangeListener { _, hasFocus ->

            if (!hasFocus) {

                val currentEmail = email.text.toString().trim()

                if (currentEmail != verifiedEmail) {

                    isEmailVerified = false
                    verifiedEmail = null

                    otpFrame.visibility = View.GONE
                }
            }
        }

        verifyBtn.setOnClickListener {

            val emailValue = email.text.toString().trim()

            if (emailValue.isEmpty()) {
                email.error = "Enter your email"
                return@setOnClickListener
            }

            if (!android.util.Patterns.EMAIL_ADDRESS
                    .matcher(emailValue).matches()
            ) {
                email.error = "Enter a valid email"
                return@setOnClickListener
            }

            isEmailVerified = false
            verifiedEmail = null

            verifyBtn.isEnabled = false

            viewLifecycleOwner.lifecycleScope.launch {

                try {

                    val request = VerifyEmailRequest(emailValue)

                    val response =
                        RetrofitClient.apiService.verifyEmail(request)

                    if (response.isSuccessful) {

                        val message = response.body()?.string()

                        Toast.makeText(
                            requireContext(),
                            message ?: "OTP sent successfully",
                            Toast.LENGTH_LONG
                        ).show()

                        otpFrame.visibility = View.VISIBLE

                    } else {

                        val errorMessage =
                            response.errorBody()?.string()

                        Toast.makeText(
                            requireContext(),
                            "OTP request failed: ${errorMessage ?: response.code()}",
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

                    verifyBtn.isEnabled = true
                }
            }
        }
    }

    // Registration API
    private fun setupRegister(view: View) {
        val email = view.findViewById<AppCompatEditText>(R.id.email)
        val username = view.findViewById<AppCompatEditText>(R.id.username)
        val phone = view.findViewById<AppCompatEditText>(R.id.phoneNumber)
        val aadhaar = view.findViewById<AppCompatEditText>(R.id.Aadhaar)
        val password = view.findViewById<AppCompatEditText>(R.id.password)
        val patientCheckBox = view.findViewById<MaterialCheckBox>(R.id.patientCheckBox)
        val doctorCheckBox = view.findViewById<MaterialCheckBox>(R.id.doctorCheckBox)
        val registerBtn = view.findViewById<MaterialButton>(R.id.registerBtn)

        registerBtn.setOnClickListener {

            // Read form values
            val emailValue = email.text.toString().trim()
            val usernameValue = username.text.toString().trim()
            val phoneValue = phone.text.toString().trim()
            val aadhaarValue = aadhaar.text.toString().trim()
            val passwordValue = password.text.toString()

            // Role selection
            val backendRole = when {
                patientCheckBox.isChecked -> "USER"
                doctorCheckBox.isChecked -> "DOCTOR"
                else -> null
            }

            // Empty validation
            if (
                emailValue.isEmpty() ||
                usernameValue.isEmpty() ||
                phoneValue.isEmpty() ||
                aadhaarValue.isEmpty() ||
                passwordValue.isEmpty()
            ) {

                Toast.makeText(
                    requireContext(),
                    "Please fill all fields",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            // Email validation
            if (!android.util.Patterns.EMAIL_ADDRESS
                    .matcher(emailValue).matches()
            ) {

                email.error = "Enter a valid email"
                return@setOnClickListener
            }

            // OTP verification check
            if (!isEmailVerified || verifiedEmail != emailValue) {

                Toast.makeText(
                    requireContext(),
                    "Please verify your email first",
                    Toast.LENGTH_SHORT
                ).show()

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

            // Role validation
            if (backendRole == null) {

                Toast.makeText(
                    requireContext(),
                    "Please select your role",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            // Disable button during API call
            registerBtn.isEnabled = false

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

                    val response =
                        RetrofitClient.apiService.register(request)

                    if (response.isSuccessful) {

                        Toast.makeText(
                            requireContext(),
                            response.body() ?: "Registration successful",
                            Toast.LENGTH_LONG
                        ).show()

                        // Navigate to Login
                        parentFragmentManager.beginTransaction()
                            .replace(
                                R.id.authFragmentContainer,
                                Login()
                            )
                            .commit()

                    } else {

                        val errorMessage =
                            response.errorBody()?.string()

                        Toast.makeText(
                            requireContext(),
                            "Registration failed: ${errorMessage ?: response.code()}",
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

    // Login navigation
    private fun setupLoginNavigation(view: View) {

        val tvLogin =
            view.findViewById<TextView>(R.id.tvLogin)

        tvLogin.setOnClickListener {

            parentFragmentManager.beginTransaction()
                .replace(
                    R.id.authFragmentContainer,
                    Login()
                )
                .addToBackStack("SignUp")
                .commit()

            (requireActivity() as? AppCompatActivity)
                ?.supportActionBar?.title = "Login"
        }
    }
}
