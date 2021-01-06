package com.example.tabletop.main.fragment

import android.os.Bundle
import android.view.View
import android.viewbinding.library.activity.viewBinding
import android.viewbinding.library.fragment.viewBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.example.tabletop.R
import com.example.tabletop.databinding.ActivityEventFormBinding
import com.example.tabletop.databinding.FragmentEventChatBinding
import com.example.tabletop.main.adapter.ChosenGameAdapter
import com.example.tabletop.main.adapter.MessageAdapter
import com.example.tabletop.mvvm.model.Event
import com.example.tabletop.mvvm.model.helpers.Message
import com.example.tabletop.mvvm.viewmodel.EventViewModel
import com.example.tabletop.settings.SettingsManager
import com.example.tabletop.util.Extra
import com.example.tabletop.util.className
import com.example.tabletop.util.getMockGame
import com.example.tabletop.util.getMockMessage
import dev.ajkueterman.lazyviewmodels.lazyViewModels
import net.alexandroid.utils.mylogkt.logI

class EventChatFragment : BaseFragment(R.layout.fragment_event_chat) {

    override val binding: FragmentEventChatBinding by viewBinding()

    private lateinit var settingsManager: SettingsManager

    private val messageAdapter by lazy { MessageAdapter() }
    private val linearlayoutManager  = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.onCreate(savedInstanceState)
        settingsManager = SettingsManager(requireContext())
        binding.recyclerView.apply {
            layoutManager = linearlayoutManager
            adapter = messageAdapter
        }


        messageAdapter.setData(listOf(getMockMessage(),getMockMessage(),getMockMessage(),getMockMessage(),getMockMessage(),getMockMessage(),getMockMessage(),getMockMessage(),getMockMessage(),getMockMessage(),getMockMessage(),getMockMessage(),getMockMessage(), getMockMessage()))

        logI("Hello")
    }

}