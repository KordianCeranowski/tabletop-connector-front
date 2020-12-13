package com.example.tabletop.main.fragment

import android.viewbinding.library.fragment.viewBinding
import androidx.viewbinding.ViewBinding
import com.example.tabletop.R
import com.example.tabletop.databinding.FragmentEventParticipantsBinding

class EventParticipantsFragment(private val participants: List<String>)
    : BaseFragment(R.layout.fragment_event_participants) {

    override val binding: FragmentEventParticipantsBinding by viewBinding()

    // private val eventViewModel: EventViewModel by lazyActivityViewModels {
    //     EventViewModel(EventRepository)
    // }
}