package com.example.tabletop.mvvm.model.helpers

import java.io.Serializable

data class Message(
    val handle: String,
    val message:String,
    val timestamp: String,
    val id: String = ""
) : Serializable