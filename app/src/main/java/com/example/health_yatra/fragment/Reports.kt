package com.example.health_yatra.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.health_yatra.LabReport
import com.example.health_yatra.R
import com.example.health_yatra.adapter.LabReportAdapter

class LabReportsFragment : Fragment() {

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

        val recyclerView =
            view.findViewById<RecyclerView>(R.id.recyclerLabReports)

        val reports = listOf(
            LabReport("CBC Report", "Blood Test", "20 Aug 2026", "🧪"),
            LabReport("LFT Report", "Blood Test", "18 Aug 2026", "🧬"),
            LabReport("KFT Report", "Kidney Test", "15 Aug 2026", "🫘"),
            LabReport("Blood Sugar", "Blood Test", "12 Aug 2026", "🩸")
        )

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = LabReportAdapter(reports)

        return view
    }
}