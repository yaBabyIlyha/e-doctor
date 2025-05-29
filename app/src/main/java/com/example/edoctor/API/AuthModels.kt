package com.example.edoctor.API

import java.time.LocalDateTime
import kotlinx.serialization.Serializable

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
    val senderLogin: String,
    val receiverLogin: String,
    val content: String,
    val timestamp: String? = null // если нужно отображать время
)

@Serializable
data class DoctorLoginRequest(
    val id: String,
    val password: String
)

data class ChatItem(
    val userLogin: String,
    val lastMessage: String
)

