package com.example.health_yatra.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.health_yatra.R
import androidx.recyclerview.widget.RecyclerView
import com.example.health_yatra.LabReport


class LabReportAdapter(private val reports: List<LabReport>): RecyclerView.Adapter<LabReportAdapter.LabReportsViewHolder>() {

    class LabReportsViewHolder(itemView : View): RecyclerView.ViewHolder(itemView){

        val reportName: TextView =
            itemView.findViewById(R.id.tvReportName)

        val reportType: TextView =
            itemView.findViewById(R.id.tvReportType)

        val reportDate: TextView =
            itemView.findViewById(R.id.tvReportDate)

        val reportIcon: TextView =
            itemView.findViewById(R.id.tvReportIcon)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): LabReportsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.item_reports,
                parent,
                false
            )

        return LabReportsViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: LabReportsViewHolder,
        position: Int,
    ) {
        val report = reports[position]
        holder.reportName.text = report.name
        holder.reportType.text = report.type
        holder.reportDate.text = report.date
        holder.reportIcon.text = report.icon
    }

    override fun getItemCount(): Int {
        return reports.size
    }
}