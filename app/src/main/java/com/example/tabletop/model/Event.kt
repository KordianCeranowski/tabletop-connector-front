package com.example.tabletop.model

import java.sql.Date

data class Event(
    val name: String,
    val gamesPool: List<Game>,
    val place: Address,
    val organise: User,
    val participants: List<User>,
    val date: Date,
    val chat: List<Message>
)