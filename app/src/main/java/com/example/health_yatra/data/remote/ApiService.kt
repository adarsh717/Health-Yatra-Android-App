package com.example.health_yatra.data.remote

import com.example.health_yatra.data.model.LoginRequest
import com.example.health_yatra.data.model.LoginResponse
import com.example.health_yatra.data.model.RegisterRequest
import com.example.health_yatra.model.PatientProfile
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @POST("authorize/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>

   @POST("authorize/register")
   suspend fun register(
       @Body request: RegisterRequest
   ): Response<String>

    @GET("authorize/loggedIn")
    suspend fun getLoggedInUser(
        @Header("Authorization") token: String
    ): Response<String>

    @GET("migrant/mig/{aadhar}")
    suspend fun getPatientProfile(
        @Header("Authorization") token: String,
        @Path("aadhar") aadhar: String
    ): Response<PatientProfile>

}
