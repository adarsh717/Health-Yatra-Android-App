package com.example.health_yatra.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.health_yatra.R
import com.example.health_yatra.model.HealthRecord
import com.google.gson.Gson

class MedicalRecordDetailsFragment : Fragment() {

    companion object {
        private const val ARG_RECORD = "medical_record"

        fun newInstance(record: HealthRecord): MedicalRecordDetailsFragment {
            val fragment = MedicalRecordDetailsFragment()

            val bundle = Bundle().apply {
                putString(ARG_RECORD, Gson().toJson(record))
            }

            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(
            R.layout.fragment_medical_record_details,
            container,
            false
        )

        val json = arguments?.getString(ARG_RECORD)

        val record = json?.let {
            Gson().fromJson(it, HealthRecord::class.java)
        }

        if (record != null) {
            showRecordDetails(view, record)
        }

        return view
    }

    private fun showRecordDetails(
        view: View,
        record: HealthRecord
    ) {

        view.findViewById<TextView>(R.id.tvDetailsTitle).text =
            record.diagnosis?.takeIf { it.isNotBlank() }
                ?: record.disease?.takeIf { it.isNotBlank() }
                        ?: "Medical Record"

        view.findViewById<TextView>(R.id.tvDetailsDate).text =
            "Date: ${record.date ?: "Not available"}"

        view.findViewById<TextView>(R.id.tvDiagnosis).text =
            record.diagnosis.displayValue()

        view.findViewById<TextView>(R.id.tvDisease).text =
            record.disease.displayValue()

        view.findViewById<TextView>(R.id.tvSymptoms).text =
            record.symptoms.displayValue()

        view.findViewById<TextView>(R.id.tvMedicines).text =
            record.medicinePres.displayValue()

        view.findViewById<TextView>(R.id.tvAllergies).text =
            record.allergies.displayValue()

        view.findViewById<TextView>(R.id.tvChronicCondition).text =
            record.chronicCondition.displayValue()

        val doctor = record.docAadhar?.firstOrNull()

        val doctorName = listOfNotNull(
            doctor?.firstName,
            doctor?.middleName,
            doctor?.lastName
        ).filter { it.isNotBlank() }.joinToString(" ")

        view.findViewById<TextView>(R.id.tvDoctorName).text =
            "Name: ${doctorName.ifBlank { "Not available" }}"

        view.findViewById<TextView>(R.id.tvDoctorQualification).text =
            "Qualification: ${doctor?.qualification.displayValue()}"

        view.findViewById<TextView>(R.id.tvDoctorHospital).text =
            "Hospital: ${doctor?.hospitalName.displayValue()}"
    }

    private fun String?.displayValue(): String {
        return this?.takeIf { it.isNotBlank() } ?: "Not available"
    }
}