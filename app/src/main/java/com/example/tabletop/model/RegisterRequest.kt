package com.example.tabletop.model

data class RegisterRequest(
    val email: String,
    val nickname: String,
    val password: String
)