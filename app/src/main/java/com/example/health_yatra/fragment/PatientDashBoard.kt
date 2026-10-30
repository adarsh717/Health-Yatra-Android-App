package com.example.health_yatra.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.health_yatra.R
import com.example.health_yatra.data.local.SessionManager
import com.example.health_yatra.data.remote.RetrofitClient
import kotlinx.coroutines.launch

class PatientDashBoard : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(
            R.layout.fragment_patient_dashboard,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val labReports = view.findViewById<View>(R.id.labReports)

        labReports.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(
                    R.id.authFragmentContainer,
                    LabReportsFragment()
                )
                .addToBackStack(null)
                .commit()
        }

        val sessionManager = SessionManager(requireContext())

        val savedName = sessionManager.getPatientName()
        val savedAadhaar = sessionManager.getPatientAadhaar()

        if (savedName != null && savedAadhaar != null) {

            view.findViewById<TextView>(R.id.patientNameTv).text =
                "Hello, $savedName!"

            view.findViewById<TextView>(R.id.patientAadhaarTv).text =
                "XXXX XXXX ${savedAadhaar.takeLast(4)}"

        } else {
            loadPatientProfile(view)
        }
    }

    private fun loadPatientProfile(view: View) {

        val sessionManager = SessionManager(requireContext())


        val savedToken = sessionManager.getToken()

        if (savedToken.isNullOrBlank()) {
            Toast.makeText(
                requireContext(),
                "Session expired. Please login again.",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        val token = "Bearer $savedToken"

        viewLifecycleOwner.lifecycleScope.launch {

            try {

                //Fetch logged-in patient's Aadhaar
                val aadhaarResponse =
                    RetrofitClient.apiService.getLoggedInUser(token)

                if (!aadhaarResponse.isSuccessful) {
                    Toast.makeText(
                        requireContext(),
                        "Unable to fetch user: ${aadhaarResponse.code()}",
                        Toast.LENGTH_LONG
                    ).show()
                    return@launch
                }

                val aadhaar = aadhaarResponse.body()?.trim()

                if (aadhaar.isNullOrBlank()) {
                    Toast.makeText(
                        requireContext(),
                        "Patient Aadhaar not found",
                        Toast.LENGTH_LONG
                    ).show()
                    return@launch
                }

                //Fetch patient profile
                val profileResponse =
                    RetrofitClient.apiService.getPatientProfile(
                        token,
                        aadhaar
                    )

                if (profileResponse.isSuccessful) {

                    val patient = profileResponse.body()

                    if (patient != null) {

                        val patientName =
                            listOfNotNull(
                                patient.firstName,
                                patient.middleName,
                                patient.lastName
                            )
                                .filter { it.isNotBlank() }
                                .joinToString(" ")

                        val nameTv = view.findViewById<TextView>(R.id.patientNameTv)

                        nameTv.text =
                            if (patientName.isNotBlank()) {
                                "Hello, $patientName!"
                            } else {
                                "Hello, Patient!"
                            }

                        // Display masked Aadhaar
                        val maskedAadhaar =
                            "XXXX XXXX " + aadhaar.takeLast(4)
                        val aadhaarTv =
                            view.findViewById<TextView>(R.id.patientAadhaarTv)

                        aadhaarTv.text = maskedAadhaar

                        sessionManager.savePatientDetails(
                            patientName,
                            aadhaar
                        )
                    }

                } else {
                    Toast.makeText(
                        requireContext(),
                        "Profile error: ${profileResponse.code()}",
                        Toast.LENGTH_LONG
                    ).show()
                }

            } catch (e: Exception) {

                Toast.makeText(
                    requireContext(),
                    "Error: ${e.localizedMessage}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}
