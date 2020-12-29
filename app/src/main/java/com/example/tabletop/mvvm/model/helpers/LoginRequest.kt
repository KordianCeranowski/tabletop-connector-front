package com.example.tabletop.mvvm.model.helpers

import java.io.Serializable

data class LoginRequest(
    val username: String,
    val password: String
) : Serializable