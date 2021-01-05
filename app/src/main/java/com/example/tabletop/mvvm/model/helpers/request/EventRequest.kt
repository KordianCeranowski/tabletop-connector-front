package com.example.tabletop.mvvm.model.helpers.request

import com.example.tabletop.mvvm.model.Game
import com.example.tabletop.mvvm.model.helpers.Address
import com.example.tabletop.mvvm.model.helpers.request.AddressSimple
import java.io.Serializable

data class EventRequest(
    val name: String,
    val date: String, //2021-12-30T16:01:00+0000
    val address: Address,
    val games: List<Game>
): Serializable