package com.example.tabletop.mvvm.model.helpers.request

import java.io.Serializable

data class AddressSimple(
    val geo_x: Double?,
    val geo_y: Double?,
) : Serializable