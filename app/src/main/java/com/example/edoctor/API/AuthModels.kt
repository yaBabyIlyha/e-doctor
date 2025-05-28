package com.example.edoctor.API

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