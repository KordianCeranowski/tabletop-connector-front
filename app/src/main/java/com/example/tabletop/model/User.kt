package com.example.tabletop.model

import java.io.Serializable

data class User(
    val email: String,
    val username: String,
    val password: String,
    // val firstName: String,
    // val lastName: String,
    // val phoneNumber: String,
    // val address: Address,
    val id: String = ""
) : Serializable