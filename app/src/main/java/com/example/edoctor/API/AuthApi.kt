package com.example.edoctor.API


import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface AuthApi {
    @POST("login")
    fun login(@Body request: LoginRequest): Call<AuthResponse>

    @POST("register")
    fun register(@Body request: RegisterRequest): Call<AuthResponse>

    @GET("user")
    fun getUserData(@Header("Authorization") token: String): Call<UserDataResponse>

    @GET("appointment/next")
    fun getNextAppointment(@Header("Authorization") token: String): Call<AppointmentResponse>

    @POST("doctor/login")
    suspend fun doctorLogin(@Body request: DoctorLoginRequest): Response<Unit>


}