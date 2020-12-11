package com.example.tabletop.model

data class User(
    val email: String,
    val username: String,
    val password: String,
    val id: String = ""
) : Model()