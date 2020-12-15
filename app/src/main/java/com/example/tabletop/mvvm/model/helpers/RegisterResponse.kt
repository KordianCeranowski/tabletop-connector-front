package com.example.tabletop.mvvm.model.helpers

import java.io.Serializable

data class RegisterResponse(
    val email: String,
    val username: String,
    val id: String
) : Serializable