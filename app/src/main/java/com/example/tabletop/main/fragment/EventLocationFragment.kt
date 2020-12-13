package com.example.tabletop.main.fragment

import android.viewbinding.library.fragment.viewBinding
import androidx.viewbinding.ViewBinding
import com.example.tabletop.R
import com.example.tabletop.databinding.FragmentEventLocationBinding
import com.example.tabletop.mvvm.model.helpers.Address

class EventLocationFragment(private val address: Address)
    : BaseFragment(R.layout.fragment_event_location) {

    override val binding: FragmentEventLocationBinding by viewBinding()

    // private val eventViewModel: EventViewModel by lazyActivityViewModels {
    //     EventViewModel(EventRepository)
    // }
}