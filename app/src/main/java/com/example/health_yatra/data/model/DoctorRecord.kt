package com.example.health_yatra.model

import com.google.gson.annotations.SerializedName

data class DoctorRecord(

    @SerializedName("id")
    val id: Int,

    @SerializedName("first_name")
    val firstName: String?,

    @SerializedName("middle_name")
    val middleName: String?,

    @SerializedName("last_name")
    val lastName: String?,

    @SerializedName("medical_regi")
    val medicalRegi: String?,

    @SerializedName("qualification")
    val qualification: String?,

    @SerializedName("specilization")
    val specialization: String?,

    @SerializedName("exp")
    val experience: Int?,

    @SerializedName("license_no")
    val licenseNo: String?,

    @SerializedName("license_validity")
    val licenseValidity: String?,

    @SerializedName("hospital_name")
    val hospitalName: String?,

    @SerializedName("working_loc")
    val workingLocation: String?,

    @SerializedName("aadhar")
    val aadhar: String?
)