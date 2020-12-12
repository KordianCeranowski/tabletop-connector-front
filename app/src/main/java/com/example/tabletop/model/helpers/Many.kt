package com.example.tabletop.model.helpers

import com.example.tabletop.model.Model
import java.io.Serializable

data class Many<T : Model>(
    val count: Int,
    val next: String? = null,
    val previous: String? = null,
    val results: List<T>
) : Serializable