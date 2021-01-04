package com.example.tabletop.mvvm.model

data class Game(
    val name: String,
    val image: String,
    val thumbnail: String,
    val min_players: Int,
    val max_players: Int,
    val playtime: Int,
    val id: String = ""
) : Model()