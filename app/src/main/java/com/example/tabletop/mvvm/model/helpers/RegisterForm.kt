package com.example.tabletop.mvvm.model.helpers

import com.example.tabletop.mvvm.model.helpers.request.ProfileSimple
import java.io.Serializable

data class RegisterForm(
    val email: String,
    val username: String,
    val password: String,
    val confirmPassword: String,
    val profile: ProfileSimple
) : Serializable