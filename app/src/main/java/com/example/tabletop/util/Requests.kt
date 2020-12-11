package com.example.tabletop.util

data class LoginRequest(
    val nickname: String,
    val password: String
)

data class RegisterRequest(
    val email: String,
    val nickname: String,
    val password: String
)

data class Form(
    val email: String,
    val nickname: String,
    val password: String,
    val confirmPassword: String
)