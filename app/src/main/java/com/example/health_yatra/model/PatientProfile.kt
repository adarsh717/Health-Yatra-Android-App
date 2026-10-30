package com.example.health_yatra.model

import com.google.gson.annotations.SerializedName

data class PatientProfile(

    @SerializedName("first_name")
    val firstName: String?,

    @SerializedName("middle_name")
    val middleName: String?,

    @SerializedName("last_name")
    val lastName: String?,

    val age: Int?,
    val gender: String?,

    @SerializedName("blood_g")
    val bloodGroup: String?,

    @SerializedName("add_curr")
    val currentAddress: String?,

    @SerializedName("add_native")
    val nativeAddress: String?,

    @SerializedName("emergency_c")
    val emergencyContact: String?,

    val occupation: String?,
    val aadhar: String?
)