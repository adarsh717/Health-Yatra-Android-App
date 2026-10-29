package com.example.health_yatra.data.remote

import com.example.health_yatra.data.model.LoginRequest
import com.example.health_yatra.data.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("authorize/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>

}
