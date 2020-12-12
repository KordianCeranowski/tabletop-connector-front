package com.example.tabletop.model

import com.example.tabletop.model.helpers.Address
import com.example.tabletop.model.helpers.Message

data class Event(
    val name: String,
    val creator: String,
    val date: String?, // todo [2020-12-25T19:27]
    val address: Address,
    val participants: List<String>,
    val games: List<Game>,
    val chat: List<Message>? = null,
    val id: String = ""
) : Model()