package com.example.health_yatra.model

import com.google.gson.annotations.SerializedName

data class HealthRecord(

    @SerializedName("id")
    val id: Int,

    @SerializedName("date")
    val date: String?,

    @SerializedName("symptoms")
    val symptoms: String?,

    @SerializedName("diagnosis")
    val diagnosis: String?,

    @SerializedName("medicine_pres")
    val medicinePres: String?,

    @SerializedName("disease")
    val disease: String?,

    @SerializedName("allergis")
    val allergies: String?,

    @SerializedName("chronic_cond")
    val chronicCondition: String?,

    @SerializedName("migAadhar")
    val migAadhar: String?,

    @SerializedName("docAadhar")
    val docAadhar: List<DoctorRecord>?
)