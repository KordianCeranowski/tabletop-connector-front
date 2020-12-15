package com.example.tabletop.mvvm.model.helpers

import java.io.Serializable

data class LoginResponse(
    val refresh: String,
    val access: String
) : Serializable
