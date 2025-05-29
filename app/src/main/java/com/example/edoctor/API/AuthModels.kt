package com.example.edoctor.API

import java.time.LocalDateTime

data class LoginRequest(
    val login: String,
    val password: String
)

data class RegisterRequest(
    val login: String,
    val password: String,
    val email: String
)

data class AuthResponse (
    val token: String
)

data class UserDataResponse (
    val login: String,
    val email: String,
    val value: String
)

data class AppointmentResponse(
    val appointmentId: Int,
    val doctorId: String,
    val dateTime: String,
    val doctorFirstName: String,
    val doctorSecondName: String,
    val doctorSpecialization: String
)

data class Message(
    val sender: String,
    val receiverDoctorId: String,
    val content: String,
    val timestamp: LocalDateTime // или LocalDateTime, если парсишь
)
