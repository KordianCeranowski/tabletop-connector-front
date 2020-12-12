package com.example.tabletop.model.helpers

import java.io.Serializable

data class LoginForm(
    val username: String,
    val password: String
) : Serializable