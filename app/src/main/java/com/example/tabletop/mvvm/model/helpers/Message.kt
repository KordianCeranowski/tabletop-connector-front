package com.example.tabletop.mvvm.model.helpers

import java.io.Serializable

data class Message(
    var handle: String?,
    val message:String,
    val timestamp: String,
    val id: String = ""
) : Serializable