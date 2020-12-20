package com.example.tabletop.mvvm.model.helpers

import com.google.gson.JsonElement
import java.io.Serializable

data class LoginResponse(
    val refresh: String,
    val access: String
    //,val detail: String,
) : Serializable

