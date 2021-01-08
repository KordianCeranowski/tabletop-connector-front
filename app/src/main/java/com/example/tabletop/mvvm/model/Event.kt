package com.example.tabletop.mvvm.model

import android.location.Location
import com.example.tabletop.mvvm.model.helpers.Address
import com.example.tabletop.mvvm.model.helpers.Chat

data class Event(
    val name: String,
    val creator: User,
    val date: String,
    val address: Address,
    val participants: List<User>,
    val games: List<Game>,
    val chat: Chat? = null,
    val id: String = ""
) : Model() {
    fun getDistanceInMetersFrom(longitude: Double, latitude: Double): Double {
        val locationA = Location("A").apply {
            setLongitude(longitude)
            setLatitude(latitude)
        }
        val locationB = Location("B").apply {
            setLongitude(address.geo_x!!)
            setLatitude(address.geo_y!!)
        }
        return locationA.distanceTo(locationB).toDouble()
    }
}