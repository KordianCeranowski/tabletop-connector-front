package com.example.tabletop.activity.sample

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.example.tabletop.databinding.ActivityLocationBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import net.alexandroid.utils.mylogkt.logW
import splitties.toast.UnreliableToastApi
import splitties.toast.toast

@UnreliableToastApi
class LocationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLocationBinding

    private val LOCATION_PERMISSION_REQ_CODE = 1000

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var latitude = 0.0
    private var longitude = 0.0

    private fun setup() {
        binding = ActivityLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                // getting the last known or current location
                latitude = location.latitude
                longitude = location.latitude
                logW("Latitude: ${location.latitude}")
                logW("Longitude: ${location.longitude}")
            }
            .addOnFailureListener {
                toast("Failed on getting current location")
            }
    }
}