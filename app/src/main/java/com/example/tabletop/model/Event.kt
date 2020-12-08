package com.example.tabletop.model

import java.io.Serializable

data class Event(
    val name: String,
    val creator: String?,
    val date: String?, // todo Date ?
    val address: Address,
    //val participants: List<User>,
    //val games: List<Game>,
    val chat: List<Message>?,
    val id: String = ""
) : Serializable