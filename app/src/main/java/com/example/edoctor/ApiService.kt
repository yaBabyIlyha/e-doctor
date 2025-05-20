package com.example.edoctor

import android.telecom.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("doctors")
    suspend fun getDoctors(): List<DoctorResponse>
}