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

class EventLocationFragment : BaseFragment(R.layout.fragment_event_location) {

    override val binding: FragmentEventLocationBinding by viewBinding()


}