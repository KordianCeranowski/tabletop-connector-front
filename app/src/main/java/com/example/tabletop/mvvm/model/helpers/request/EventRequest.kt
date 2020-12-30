package com.example.tabletop.mvvm.model.helpers.request

import com.example.tabletop.mvvm.model.helpers.request.AddressSimple
import java.io.Serializable

data class EventRequest(
    val name: String,
    val date: String,
    val address: AddressSimple
): Serializable