package com.example.tabletop.main.fragment

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import com.example.tabletop.R
import com.example.tabletop.databinding.FragmentEventInfoBinding
import com.example.tabletop.mvvm.model.Event
import com.example.tabletop.util.className
import net.alexandroid.utils.mylogkt.logI

class EventInfoFragment : BaseFragment(R.layout.fragment_event_info) {

    override val binding: FragmentEventInfoBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        logI("Created ${this.className}")

        val event = arguments?.getSerializable("EVENT") as Event

        binding.tvEventName.text = event.name
        binding.tvEventCreator.text = event.creator.username
        binding.tvEventDate.text = event.date
    }
}