package com.example.edoctor.API

import com.example.edoctor.doctor.DoctorResponse
import retrofit2.http.GET

interface ApiService {
    @GET("doctors")
    suspend fun getDoctors(): List<DoctorResponse>
}