package com.example.health_yatra.fragment

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.health_yatra.R
import com.example.health_yatra.data.local.SessionManager
import com.example.health_yatra.data.remote.RetrofitClient
import androidx.lifecycle.lifecycleScope
import com.example.health_yatra.Home
import kotlinx.coroutines.launch
class DoctorPatientsFragment : Fragment() {


    lateinit var logoutBtn : Button
    private lateinit var etSearchAadhaar: EditText
    private lateinit var btnSearchPatient: Button
    private lateinit var btnAddPatient: Button
    private lateinit var cardPatientResult: View
    private lateinit var sessionManager: SessionManager
    private lateinit var tvPatientName: TextView
    private lateinit var tvPatientDetails: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return inflater.inflate(
            R.layout.fragment_doctor_patient,
            container,
            false
        )
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(requireContext())

        etSearchAadhaar = view.findViewById(R.id.etSearchAadhaar)
        btnSearchPatient = view.findViewById(R.id.btnSearchPatient)
        btnAddPatient = view.findViewById(R.id.btnAddPatient)
        cardPatientResult = view.findViewById(R.id.cardPatientResult)

        tvPatientName = view.findViewById(R.id.tvPatientName)
        tvPatientDetails = view.findViewById(R.id.tvPatientDetails)
        logoutBtn = view.findViewById(R.id.logOutButton)

        cardPatientResult.visibility = View.GONE

        btnSearchPatient.setOnClickListener {
            searchPatient()
        }

        btnAddPatient.setOnClickListener {
            showAddPatientDialog()
        }

        logoutBtn.setOnClickListener {

            val sessionManager = SessionManager(requireContext())

            sessionManager.clearSession()
            val intent = Intent(requireContext(), Home::class.java)

            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)

            requireActivity().finish()

            parentFragmentManager.beginTransaction()
                .replace(
                    R.id.authFragmentContainer,
                    Login()
                )
                .addToBackStack(null)
                .commit()
        }

    }

    // Search patient using Aadhaar

    private fun searchPatient() {

        val aadhaar = etSearchAadhaar.text.toString().trim()

        // 1. Validate Aadhaar
        if (!aadhaar.matches(Regex("\\d{12}"))) {
            etSearchAadhaar.error = "Enter valid 12 digit Aadhaar"
            etSearchAadhaar.requestFocus()
            return
        }

        // Hide previous patient result
        cardPatientResult.visibility = View.GONE

        viewLifecycleOwner.lifecycleScope.launch {

            btnSearchPatient.isEnabled = false
            btnSearchPatient.text = "Searching..."

            try {

                // 2. Get saved JWT token
                val token = sessionManager.getToken()

                if (token.isNullOrBlank()) {

                    Toast.makeText(
                        requireContext(),
                        "Session expired. Please login again",
                        Toast.LENGTH_SHORT
                    ).show()

                    return@launch
                }

                // 3. Call Search Patient API
                val response = RetrofitClient.apiService
                    .searchPatientByAadhar(
                        "Bearer $token",
                        aadhaar
                    )

                // 4. Handle API response
                when {

                    response.isSuccessful -> {

                        val patient = response.body()

                        if (patient != null) {

                            // 5. Display patient details
                            val fullName = listOfNotNull(
                                patient.firstName,
                                patient.middleName?.takeIf {
                                    it.isNotBlank()
                                },
                                patient.lastName
                            ).joinToString(" ")

                            tvPatientName.text = fullName

                            val maskedAadhaar =
                                "XXXX-XXXX-${aadhaar.takeLast(4)}"

                            val age = getString(
                                R.string.patient_age,
                                patient.age
                            )

                            val gender = getString(
                                R.string.patient_gender,
                                patient.gender
                            )

                            val bloodGroup = getString(
                                R.string.patient_blood_group,
                                patient.bloodGroup ?: "N/A"
                            )

                            val occupation = getString(
                                R.string.patient_occupation,
                                patient.occupation ?: "N/A"
                            )

                            val aadhaarText = getString(
                                R.string.patient_aadhaar,
                                maskedAadhaar
                            )

                            tvPatientDetails.text = getString(
                                R.string.patient_details,
                                age,
                                gender,
                                bloodGroup,
                                occupation,
                                aadhaarText
                            )

                            cardPatientResult.visibility = View.VISIBLE

                            Toast.makeText(
                                requireContext(),
                                "Patient found successfully",
                                Toast.LENGTH_SHORT
                            ).show()

                        } else {

                            Toast.makeText(
                                requireContext(),
                                "Patient data not available",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    response.code() == 404 -> {

                        Toast.makeText(
                            requireContext(),
                            "No patient found with this Aadhaar",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    response.code() == 401 ||
                            response.code() == 403 -> {

                        Toast.makeText(
                            requireContext(),
                            "Session expired or access denied",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    else -> {

                        Toast.makeText(
                            requireContext(),
                            "Search failed: HTTP ${response.code()}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

            } catch (e: Exception) {

                Toast.makeText(
                    requireContext(),
                    "Error: ${e.localizedMessage ?: "Something went wrong"}",
                    Toast.LENGTH_LONG
                ).show()

            } finally {

                btnSearchPatient.isEnabled = true
                btnSearchPatient.text = "Search Patient"
            }
        }
    }



    // Add new patient dialog
    private fun showAddPatientDialog() {

        val form = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(40, 10, 40, 0)
        }

        fun createField(
            hintText: String,
            type: Int = InputType.TYPE_CLASS_TEXT
        ): EditText {

            return EditText(requireContext()).apply {
                hint = hintText
                inputType = type
                textSize = 15f

                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
            }.also {
                form.addView(it)
            }
        }

        val etName = createField("Patient Full Name")

        val etAadhaar = createField(
            "12 digit Aadhaar",
            InputType.TYPE_CLASS_NUMBER
        ).apply {
            filters = arrayOf(InputFilter.LengthFilter(12))
        }

        val etAge = createField(
            "Age",
            InputType.TYPE_CLASS_NUMBER
        )

        val etMobile = createField(
            "Mobile Number",
            InputType.TYPE_CLASS_PHONE
        )

        val etGender = createField("Gender (Male/Female/Other)")

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Register New Patient")
            .setView(form)
            .setNegativeButton("Cancel", null)
            .setPositiveButton("Add Patient", null)
            .create()

        dialog.setOnShowListener {

            dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setOnClickListener {

                    val name = etName.text.toString().trim()
                    val aadhaar = etAadhaar.text.toString().trim()
                    val age = etAge.text.toString().trim()
                    val mobile = etMobile.text.toString().trim()
                    val gender = etGender.text.toString().trim()

                    when {
                        name.isEmpty() -> {
                            etName.error = "Enter patient name"
                        }

                        aadhaar.length != 12 -> {
                            etAadhaar.error = "Enter valid Aadhaar"
                        }

                        age.isEmpty() -> {
                            etAge.error = "Enter age"
                        }

                        mobile.length != 10 -> {
                            etMobile.error = "Enter valid mobile number"
                        }

                        gender.isEmpty() -> {
                            etGender.error = "Enter gender"
                        }

                        else -> {

                            Toast.makeText(
                                requireContext(),
                                "This service is temporarily on hold. Please check back later.",
                                Toast.LENGTH_SHORT
                            ).show()

                            dialog.dismiss()
                        }
                    }
                }
        }
        dialog.show()
        // Add button color
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            .setTextColor(Color.parseColor("#2196F3"))

// Cancel button color
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            .setTextColor(Color.parseColor("#757575"))
    }
}
