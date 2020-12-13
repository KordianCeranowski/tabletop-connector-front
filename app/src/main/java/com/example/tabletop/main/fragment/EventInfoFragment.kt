package com.example.tabletop.main.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.viewbinding.library.fragment.viewBinding
import androidx.viewbinding.ViewBinding
import com.example.tabletop.R
import com.example.tabletop.databinding.FragmentEventInfoBinding
import com.example.tabletop.mvvm.model.Event
import net.alexandroid.utils.mylogkt.logI

class EventInfoFragment(private val event: Event) : BaseFragment(R.layout.fragment_event_info) {

    override val binding: FragmentEventInfoBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        logI("Created $event")

        binding.tvEventName.text = event.name
        binding.tvEventCreator.text = event.creator
        binding.tvEventDate.text = event.date
    }
}