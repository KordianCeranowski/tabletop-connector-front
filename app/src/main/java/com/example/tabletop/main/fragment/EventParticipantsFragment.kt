package com.example.tabletop.main.fragment

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tabletop.R
import com.example.tabletop.databinding.FragmentEventParticipantsBinding
import com.example.tabletop.main.adapter.ParticipantAdapter
import com.example.tabletop.mvvm.model.Event
import com.example.tabletop.util.*
import net.alexandroid.utils.mylogkt.logI

class EventParticipantsFragment : BaseFragment(R.layout.fragment_event_participants) {

    override val binding: FragmentEventParticipantsBinding by viewBinding()

    private val participantAdapter by lazy { ParticipantAdapter() }

    fun setup() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = participantAdapter
            addItemDecoration(
                DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()

        logI("Created ${this.className}")

        val participants = (arguments?.getSerializable(Extra.EVENT()) as Event).participants

        if (participants.isEmpty()) {
            binding.tvEmptyList.text = "No participants to show :("
        } else {
            participantAdapter.setData(participants)
        }
    }
}