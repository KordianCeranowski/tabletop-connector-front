package com.example.tabletop.mvvm.model.helpers

import java.io.Serializable

data class Profile(
    val firstname: String?, //todo change to non nullable
    val lastname: String?,  //todo change to non nullable
    val image: String?,
    val id: String = ""
) : Serializable