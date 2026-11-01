package com.example.health_yatra.repository

import com.example.health_yatra.data.remote.RetrofitClient
import com.example.health_yatra.model.HealthRecord
import retrofit2.Response

class HealthRecordRepository {

    suspend fun fetchHealthRecords(
        token: String,
        aadhar: String
    ): Response<List<HealthRecord>> {

        return RetrofitClient.apiService.fetchHealthRecords(
            token = "Bearer $token",
            aadhar = aadhar
        )
    }
}