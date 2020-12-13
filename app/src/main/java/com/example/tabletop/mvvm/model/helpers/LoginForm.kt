package com.example.tabletop.mvvm.model.helpers

import java.io.Serializable

data class LoginForm(
    val username: String,
    val password: String
) : Serializable