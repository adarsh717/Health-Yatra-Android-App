package com.example.health_yatra.data.remote

import com.example.health_yatra.data.model.LoginRequest
import com.example.health_yatra.data.model.LoginResponse
import com.example.health_yatra.data.model.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("authorize/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>

   @POST("authorize/register")
   suspend fun register(
       @Body request: RegisterRequest
   ): Response<String>

}
