package com.example.tabletop.model

data class Game(
    val name: String,
    val image: String,
    val minPlayers: Int,
    val maxPlayers: Int,
    val playTime: Int,
    val id: String = ""
) : Model()