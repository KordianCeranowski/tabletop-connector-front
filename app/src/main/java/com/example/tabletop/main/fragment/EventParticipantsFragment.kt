package com.example.tabletop.main.fragment

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tabletop.R
import com.example.tabletop.databinding.FragmentEventParticipantsBinding
import com.example.tabletop.main.adapter.ParticipantAdapter
import com.example.tabletop.mvvm.model.User
import com.example.tabletop.util.className
import com.example.tabletop.util.getMockProfile
import net.alexandroid.utils.mylogkt.logI

class EventParticipantsFragment : BaseFragment(R.layout.fragment_event_participants) {

    override val binding: FragmentEventParticipantsBinding by viewBinding()

    private val participantAdapter by lazy { ParticipantAdapter() }

    fun setup() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = participantAdapter
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()

        logI("Created ${this.className}")

        val sampleParticipants = List(6) {
            User ("Email ${it + 1}",
                "User ${it + 1}",
                getMockProfile()
            )
        }

        //val participants = (arguments?.getSerializable("EVENT") as Event).participants

        participantAdapter.setData(sampleParticipants)
    }
}