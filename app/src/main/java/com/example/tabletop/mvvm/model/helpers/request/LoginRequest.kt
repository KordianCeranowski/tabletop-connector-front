package com.example.tabletop.mvvm.model.helpers.request

import java.io.Serializable

data class LoginRequest(
    val username: String,
    val password: String
) : Serializable