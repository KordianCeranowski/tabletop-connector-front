package com.example.tabletop.model.helpers

import java.io.Serializable

data class Form(
    val email: String,
    val username: String,
    val password: String,
    val confirmPassword: String
) : Serializable