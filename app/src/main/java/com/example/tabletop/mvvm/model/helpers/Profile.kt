package com.example.tabletop.mvvm.model.helpers

import java.io.Serializable

data class Profile(
    val firstName: String?,
    val lastName: String?,
    val image: String?,
    val id: String = ""
) : Serializable