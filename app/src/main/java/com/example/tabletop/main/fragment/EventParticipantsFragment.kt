package com.example.tabletop.main.fragment

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tabletop.R
import com.example.tabletop.databinding.FragmentEventParticipantsBinding
import com.example.tabletop.main.adapter.ParticipantAdapter
import com.example.tabletop.mvvm.model.Event
import com.example.tabletop.mvvm.model.User
import com.example.tabletop.mvvm.model.helpers.Profile
import com.example.tabletop.mvvm.viewmodel.EventViewModel
import com.example.tabletop.settings.SettingsManager
import com.example.tabletop.util.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import net.alexandroid.utils.mylogkt.logE
import net.alexandroid.utils.mylogkt.logI
import retrofit2.Response

class EventParticipantsFragment : BaseFragment(R.layout.fragment_event_participants) {

    override val binding: FragmentEventParticipantsBinding by viewBinding()

    private val participantAdapter by lazy { ParticipantAdapter() }

    private lateinit var settingsManager: SettingsManager

    fun setup() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = participantAdapter
        }
        settingsManager = SettingsManager(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()

        logI("Created ${this.className}")

        val eventId = (arguments?.getSerializable("EVENT") as Event).id

        EventViewModel.responseOne.observe(viewLifecycleOwner) { handleResponse(it) }

        lifecycleScope.launch {
            val accessToken = settingsManager.userAccessTokenFlow.first()
            EventViewModel.getOne(accessToken, eventId)
        }
    }

    private fun handleResponse(response: Response<Event>){
        val onSuccess = {
            response.body()?.let { participantAdapter.setData(it.participants) } as Unit
        }

        val onFailure = {
            logI(response.getFullResponse())
            response.getErrorBodyProperties()
        }

        response.resolve(onSuccess, onFailure)
    }
}