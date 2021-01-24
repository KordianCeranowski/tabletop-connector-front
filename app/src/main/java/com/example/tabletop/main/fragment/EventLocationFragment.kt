package com.example.tabletop.main.fragment

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import com.example.tabletop.R
import com.example.tabletop.databinding.FragmentEventLocationBinding
import com.example.tabletop.mvvm.model.Event
import com.example.tabletop.util.Extra
import net.alexandroid.utils.mylogkt.logV

class EventLocationFragment : BaseFragment(R.layout.fragment_event_location) {

    override val binding: FragmentEventLocationBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val address = (arguments?.getSerializable(Extra.EVENT.toString()) as Event).address
        binding.run {
            logV(address.toString())
            address.let {
                tvEventCountry.text = it.country
                tvEventCity.text = it.city
                tvEventStreetAndNumber.text = "${it.street} ${it.number}"
                tvEventPostalCode.text = it.postal_code
            }
        }
    }
}