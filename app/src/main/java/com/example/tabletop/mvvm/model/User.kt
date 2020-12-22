package com.example.tabletop.mvvm.model

import com.example.tabletop.mvvm.model.helpers.Profile

data class User(
    val email: String,
    val username: String,
    val password: String,
    val profile: Profile,
    val id: String = ""
) : Model()