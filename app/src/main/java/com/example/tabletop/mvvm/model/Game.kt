package com.example.tabletop.mvvm.model

data class Game(
    val name: String,
    val image: String,
    val thumbnail: String,
    val minPlayers: Int,
    val maxPlayers: Int,
    val playTime: Int,
    val id: String = ""
) : Model()