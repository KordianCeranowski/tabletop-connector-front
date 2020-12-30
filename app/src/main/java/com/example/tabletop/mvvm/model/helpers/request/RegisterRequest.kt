package com.example.tabletop.mvvm.model.helpers.request

import java.io.Serializable

data class RegisterRequest(
    val email: String,
    val username: String,
    val password: String,
    val profile: ProfileSimple
) : Serializable