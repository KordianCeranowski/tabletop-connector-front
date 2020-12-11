package com.example.tabletop.model.helpers

import com.example.tabletop.model.Model

data class Post(
    val userId: Int,
    val id: Int,
    val title: String,
    val body: String
) : Model()