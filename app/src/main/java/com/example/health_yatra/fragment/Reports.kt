package com.example.health_yatra.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.health_yatra.R
import com.example.health_yatra.adapter.LabReportAdapter
import com.example.health_yatra.data.local.SessionManager
import com.example.health_yatra.repository.HealthRecordRepository
import kotlinx.coroutines.launch

class LabReportsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvEmptyReports: TextView

    private val repository = HealthRecordRepository()
    private var isDataLoaded = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(
            R.layout.fragment_reports,
            container,
            false
        )

        recyclerView = view.findViewById(R.id.recyclerLabReports)
        progressBar = view.findViewById(R.id.progressBar)
        tvEmptyReports = view.findViewById(R.id.tvEmptyReports)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())


            fetchHealthRecords()


        return view
    }

    private fun fetchHealthRecords() {

        val sessionManager = SessionManager(requireContext())

        val token = sessionManager.getToken()
        val aadhar = sessionManager.getPatientAadhaar()

        if (token.isNullOrBlank() || aadhar.isNullOrBlank()) {
            Toast.makeText(
                requireContext(),
                "Session or Aadhaar not found. Please login again.",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        progressBar.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
        tvEmptyReports.visibility = View.GONE

        viewLifecycleOwner.lifecycleScope.launch {

            try {

                val response = repository.fetchHealthRecords(
                    token = token,
                    aadhar = aadhar
                )

                if (!isAdded) return@launch

                progressBar.visibility = View.GONE

                if (response.isSuccessful) {

                    val records = response.body()

                    if (!records.isNullOrEmpty()) {

                        recyclerView.adapter = LabReportAdapter(records) { selectedRecord ->

                            // Get the current fragment container ID dynamically
                            val container = view?.parent as? ViewGroup

                            if (container == null || container.id == View.NO_ID) {
                                Toast.makeText(
                                    requireContext(),
                                    "Unable to open details screen.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@LabReportAdapter
                            }

                            val detailsFragment =
                                MedicalRecordDetailsFragment.newInstance(selectedRecord)

                            parentFragmentManager.beginTransaction()
                                .replace(container.id, detailsFragment)
                                .addToBackStack("medical_record_details")
                                .commit()
                        }

                        recyclerView.visibility = View.VISIBLE
                        tvEmptyReports.visibility = View.GONE

                    } else {

                        recyclerView.visibility = View.GONE
                        tvEmptyReports.visibility = View.VISIBLE
                        tvEmptyReports.text = "No medical records found."
                    }

                } else {

                    recyclerView.visibility = View.GONE
                    tvEmptyReports.visibility = View.VISIBLE

                    Toast.makeText(
                        requireContext(),
                        "Failed to load records: ${response.code()}",
                        Toast.LENGTH_LONG
                    ).show()
                }

            } catch (e: Exception) {

                if (!isAdded) return@launch

                progressBar.visibility = View.GONE

                Toast.makeText(
                    requireContext(),
                    "Error: ${e.localizedMessage}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}