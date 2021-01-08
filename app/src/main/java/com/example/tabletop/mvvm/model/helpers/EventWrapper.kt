package com.example.tabletop.mvvm.model.helpers

import com.example.tabletop.mvvm.model.Event

data class EventWrapper(
    val event: Event,
    val distance: Double
)