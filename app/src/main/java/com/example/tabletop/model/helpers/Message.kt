package com.example.tabletop.model.helpers

import java.io.Serializable


data class Message(
    val handle: String,
    val content: String,
    val id: String = ""
) : Serializable