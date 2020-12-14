package com.example.tabletop.main.activity.sample

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.viewbinding.library.activity.viewBinding
import androidx.core.app.ActivityCompat
import com.example.tabletop.main.activity.BaseActivity
import com.example.tabletop.databinding.ActivityLocationBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import net.alexandroid.utils.mylogkt.logW
import splitties.toast.UnreliableToastApi
import splitties.toast.toast

@UnreliableToastApi
class LocationActivity : BaseActivity() {

    override val binding: ActivityLocationBinding by viewBinding()

    private val LOCATION_PERMISSION_REQ_CODE = 1000

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun setup() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setup()

        showCurrentLocation()
    }

    private fun showCurrentLocation() {
        // checking location permission
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // request permission
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQ_CODE
            )
            logW("DONE")
            //todo make it so after granting privileges it runs the code below
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                binding.tvLatitude.text = "Latitude: ${location.latitude}"
                binding.tvLongitude.text = "Longitude: ${location.longitude}"
            }
            .addOnFailureListener {
                toast("Failed on getting current location")
            }
    }
}