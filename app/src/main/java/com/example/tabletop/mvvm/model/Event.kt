package com.example.tabletop.mvvm.model

import com.example.tabletop.mvvm.model.helpers.Address
import com.example.tabletop.mvvm.model.helpers.Chat
import com.example.tabletop.mvvm.model.helpers.Message

data class Event(
    val name: String,
    val creator: User,
    val date: String,
    val address: Address,
    val participants: List<User>,
    val games: List<Game>,
    val chat: Chat? = null,
    val id: String = ""
) : Model()