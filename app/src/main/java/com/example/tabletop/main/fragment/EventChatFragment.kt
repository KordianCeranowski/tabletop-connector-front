package com.example.tabletop.main.fragment

import android.viewbinding.library.fragment.viewBinding
import androidx.viewbinding.ViewBinding
import com.example.tabletop.R
import com.example.tabletop.databinding.FragmentEventChatBinding
import com.example.tabletop.mvvm.model.helpers.Message

class EventChatFragment(private val chat: List<Message>) : BaseFragment(R.layout.fragment_event_chat) {

    override val binding: FragmentEventChatBinding by viewBinding()

    // private val eventViewModel: EventViewModel by lazyActivityViewModels {
    //     EventViewModel(EventRepository)
    // }
}