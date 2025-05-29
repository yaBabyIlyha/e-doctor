package com.example.edoctor.API


import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface AuthApi {
    @POST("login")
    fun login(@Body request: LoginRequest): Call<AuthResponse>

    @POST("register")
    fun register(@Body request: RegisterRequest): Call<AuthResponse>

    @GET("user")
    fun getUserData(@Header("Authorization") token: String): Call<UserDataResponse>

    @POST("doctor/login")
    suspend fun doctorLogin(@Body request: DoctorLoginRequest): Response<Unit>

    @POST("appointment")
    suspend fun createAppointment(@Body appointment: AppointmentRequest): AppointmentResponse

    @GET("appointment/next")
    fun getNextAppointmentByLogin(@Query("login") login: String): Call<AppointmentResponse>

}