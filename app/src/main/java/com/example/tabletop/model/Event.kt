package com.example.tabletop.model

import com.example.tabletop.model.helpers.Message

data class Event(
    val name: String,
    val creator: String?,
    val date: String?, // todo DateTime format
    val address: Address,
    //val participants: List<User>,
    //val games: List<Game>,
    val chat: List<Message>?,
    val id: String = ""
) : Model()