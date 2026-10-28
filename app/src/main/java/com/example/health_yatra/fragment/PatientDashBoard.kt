package com.example.health_yatra.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.health_yatra.R


class PatientDashBoard : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_patient_dashboard, container, false)
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
    }
}