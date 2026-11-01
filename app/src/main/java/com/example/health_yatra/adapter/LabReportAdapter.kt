package com.example.health_yatra.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.health_yatra.R
import com.example.health_yatra.model.HealthRecord

class LabReportAdapter(
    private val reports: List<HealthRecord>,
    private val onReportClick: (HealthRecord) -> Unit
) : RecyclerView.Adapter<LabReportAdapter.LabReportsViewHolder>() {

    class LabReportsViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val cardReport: CardView = itemView.findViewById(R.id.cardReport)
        val reportName: TextView = itemView.findViewById(R.id.tvReportName)
        val reportType: TextView = itemView.findViewById(R.id.tvReportType)
        val reportDate: TextView = itemView.findViewById(R.id.tvReportDate)
        val reportIcon: TextView = itemView.findViewById(R.id.tvReportIcon)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LabReportsViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_reports, parent, false)

        return LabReportsViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: LabReportsViewHolder,
        position: Int
    ) {
        val report = reports[position]

        holder.reportName.text =
            report.diagnosis?.takeIf { it.isNotBlank() }
                ?: report.disease?.takeIf { it.isNotBlank() }
                        ?: "Medical Record"

        holder.reportType.text =
            report.symptoms?.takeIf { it.isNotBlank() }
                ?: "Medical History"

        holder.reportDate.text =
            report.date?.takeIf { it.isNotBlank() }
                ?: "Date not available"

        holder.reportIcon.text = "🩺"

        // Card click
        holder.cardReport.setOnClickListener {
            onReportClick(report)
        }
    }

    override fun getItemCount(): Int = reports.size
}