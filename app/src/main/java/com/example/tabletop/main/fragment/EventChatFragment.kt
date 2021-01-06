package com.example.tabletop.main.fragment

import android.os.Bundle
import android.viewbinding.library.fragment.viewBinding
import androidx.viewbinding.ViewBinding
import com.example.tabletop.R
import com.example.tabletop.databinding.FragmentEventChatBinding
import com.example.tabletop.mvvm.model.helpers.Message
import net.alexandroid.utils.mylogkt.logI

class EventChatFragment : BaseFragment(R.layout.fragment_event_chat) {

    override val binding: FragmentEventChatBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        logI("Hello")
    }

}