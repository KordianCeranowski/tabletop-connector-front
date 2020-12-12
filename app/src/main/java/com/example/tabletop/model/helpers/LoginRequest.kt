package com.example.tabletop.model.helpers

import java.io.Serializable

data class LoginRequest(
    val username: String,
    val password: String
) : Serializable