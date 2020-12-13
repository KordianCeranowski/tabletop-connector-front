package com.example.tabletop.mvvm.model

data class User(
    val email: String,
    val username: String,
    val password: String,
    val id: String = ""
) : Model()