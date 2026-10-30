package com.example.health_yatra.data.model

data class RegisterRequest(
    val username: String,
    val name: String,
    val phone: String,
    val aadhar: String,
    val password: String,
    val role: String
)