package com.example.edoctor

import retrofit2.http.GET

interface ApiService {

    @GET("doctors")
    suspend fun getDoctors(): List<Doctor>
}