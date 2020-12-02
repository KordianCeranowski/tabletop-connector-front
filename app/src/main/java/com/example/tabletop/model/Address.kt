package com.example.tabletop.model

data class Address(
    val country: String,
    val city: String,
    val street: String,
    val postal_code: String,
    val number: String,
    val geo_x: Double?,
    val geo_y: Double?,
    val id: String = ""
)