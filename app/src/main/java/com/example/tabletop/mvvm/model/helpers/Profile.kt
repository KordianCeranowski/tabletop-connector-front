package com.example.tabletop.mvvm.model.helpers

import java.io.Serializable

data class Profile(
    val firstname: String,
    val lastname: String,
    val avatar: String,
    val id: String = ""
) : Serializable