package com.example.tabletop.main.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.viewbinding.library.activity.viewBinding
import android.viewbinding.library.fragment.viewBinding
import androidx.core.app.ActivityCompat
import androidx.viewbinding.ViewBinding
import com.example.tabletop.R
import com.example.tabletop.databinding.ActivityLocationBinding
import com.example.tabletop.databinding.FragmentEventLocationBinding
import com.example.tabletop.mvvm.model.helpers.Address
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import net.alexandroid.utils.mylogkt.logW
import splitties.toast.toast

class EventLocationFragment
    : BaseFragment(R.layout.fragment_event_location) {

    override val binding: FragmentEventLocationBinding by viewBinding()

    // private val LOCATION_PERMISSION_REQ_CODE = 1000
    //
    // private lateinit var fusedLocationClient: FusedLocationProviderClient
    //
    // fun setup() {
    //     fusedLocationClient = LocationServices.getFusedLocationProviderClient()
    // }
    //
    // override fun onCreate(savedInstanceState: Bundle?) {
    //     super.onCreate(savedInstanceState)
    //     setup()
    //
    //     showCurrentLocation()
    // }
    //
    // private fun showCurrentLocation() {
    //     // checking location permission
    //     if (ActivityCompat.checkSelfPermission(this,
    //             Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
    //         // request permission
    //         ActivityCompat.requestPermissions(
    //             this,
    //             arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
    //             LOCATION_PERMISSION_REQ_CODE
    //         )
    //         logW("DONE")
    //         //todo make it so after granting privileges it runs the code below
    //         return
    //     }
    //     fusedLocationClient.lastLocation
    //         .addOnSuccessListener { location ->
    //             binding.tvLatitude.text = "Latitude: ${location.latitude}"
    //             binding.tvLongitude.text = "Longitude: ${location.longitude}"
    //         }
    //         .addOnFailureListener {
    //             toast("Failed on getting current location")
    //         }
    // }
}