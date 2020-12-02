package com.example.tabletop.model

data class Game(
    val name: String,
    val genres: List<Genre>,
    val minPlayers: Int,
    val maxPlayers: Int,
    val minPlaytime: Int,
    val maxPlaytime: Int,
    val vendor: String,
    val id: String = ""
)