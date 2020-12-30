package com.example.tabletop.main.fragment

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tabletop.R
import com.example.tabletop.databinding.FragmentEventParticipantsBinding
import com.example.tabletop.main.adapter.ParticipantAdapter
import com.example.tabletop.mvvm.model.Event
import com.example.tabletop.mvvm.model.User
import com.example.tabletop.mvvm.model.helpers.Profile
import com.example.tabletop.mvvm.viewmodel.EventViewModel
import com.example.tabletop.util.className
import com.example.tabletop.util.getErrorBodyProperties
import com.example.tabletop.util.getMockProfile
import com.example.tabletop.util.resolve
import net.alexandroid.utils.mylogkt.logE
import net.alexandroid.utils.mylogkt.logI
import retrofit2.Response

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

        val eventId = (arguments?.getSerializable("EVENT") as Event).id

        EventViewModel.responseOne.observe(viewLifecycleOwner) { handleResponse(it) }

        EventViewModel.getOne(eventId)
    }

    private fun handleResponse(response: Response<Event>){
        val onSuccess = {
            response.body()?.let { participantAdapter.setData(it.participants) } as Unit
        }

        val onFailure = {
            response.getErrorBodyProperties()
        }

        response.resolve(onSuccess, onFailure)
    }
}