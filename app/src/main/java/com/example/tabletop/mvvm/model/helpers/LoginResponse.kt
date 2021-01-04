package com.example.tabletop.mvvm.model.helpers

import java.io.Serializable

data class LoginResponse(
    val auth_token: String,
    val firstname: String,
    val user_id: String
) : Serializable

