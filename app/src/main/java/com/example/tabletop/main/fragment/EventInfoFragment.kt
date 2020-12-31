package com.example.tabletop.main.fragment

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import com.example.tabletop.R
import com.example.tabletop.databinding.FragmentEventInfoBinding
import com.example.tabletop.main.activity.ProfileActivity
import com.example.tabletop.mvvm.model.Event
import com.example.tabletop.mvvm.viewmodel.EventViewModel
import com.example.tabletop.settings.SettingsManager
import com.example.tabletop.util.*
import dev.ajkueterman.lazyviewmodels.lazyActivityViewModels
import dev.ajkueterman.lazyviewmodels.lazyViewModels
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import net.alexandroid.utils.mylogkt.logD
import net.alexandroid.utils.mylogkt.logI
import net.alexandroid.utils.mylogkt.logV
import net.alexandroid.utils.mylogkt.logW
import retrofit2.Response
import splitties.toast.UnreliableToastApi
import splitties.toast.toast

@Suppress("COMPATIBILITY_WARNING")
@UnreliableToastApi
class EventInfoFragment : BaseFragment(R.layout.fragment_event_info) {

    override val binding: FragmentEventInfoBinding by viewBinding()

    private val eventViewModel by lazyActivityViewModels { EventViewModel() }

    private lateinit var settingsManager: SettingsManager

    private lateinit var currentEvent: Event

    private val accessToken: String
        get() = runBlocking { settingsManager.userAccessTokenFlow.first() }

    private val userId: String
        get() = runBlocking { settingsManager.userIdFlow.first() }

    private val participantsId: List<String>
        get() = currentEvent.participants.map { it.id }.also { logV(it.size.toString()) }

    fun setup() {
        settingsManager = SettingsManager(requireContext())
        //logI("Starting ${this.className}")
        currentEvent = arguments?.getSerializable(EXTRA_EVENT) as Event
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()

        attachObservers()

        setupOnClickListeners()
    }

    override fun onResume() {
        super.onResume()

        binding.tvEventName.text = currentEvent.name
        binding.tvEventCreator.text = currentEvent.creator.username
        binding.tvEventDate.text = currentEvent.date

        setupBtnJoinEvent()
    }

    private fun setupBtnJoinEvent() {
        binding.btnJoinEvent.let { btn ->
            if (userId == currentEvent.creator.id) {
                btn.visibility = View.INVISIBLE
            } else {
                btn.visibility = View.VISIBLE
                btn.text =
                    if (participantsId.contains(userId))
                        "Joined!".also { logI("Participant") }
                    else
                        "Join".also { logI("Not participant") }
            }
        }
    }

    private fun setupOnClickListeners() {
        binding.btnJoinEvent.setOnClickListener {
            lifecycleScope.launch {
                val accessToken = settingsManager.userAccessTokenFlow.first()
                participateInEvent(accessToken, currentEvent.id)
            }
        }

        binding.tvEventCreator.setOnClickListener {
            context?.startWithExtra<ProfileActivity>(
                EXTRA_PROFILE_ID to currentEvent.creator.profile.id
            )
        }
    }

    private fun attachObservers() {
        eventViewModel.run {
            responseParticipation.observe(viewLifecycleOwner) {
                handleResponseParticipation(it)
            }
            responseOneParticipation.observe(viewLifecycleOwner) {
                handleResponseRetrieveEvent(it)
            }
        }
    }

    private fun participateInEvent(accessToken: String, eventId: String) {
        eventViewModel.participateInEvent(accessToken, eventId)
    }

    private fun handleResponseParticipation(response: Response<Unit>) {
        val onSuccess = {
            logD(response.status())
            retrieveEvent(accessToken, currentEvent.id)
        }

        val onFailure = {
            toast("Something went wrong while joining event")
            logW(response.getFullResponse())
            logI(response.getErrorBodyProperties().toString())
        }

        response.resolve(onSuccess, onFailure)
    }

    private fun retrieveEvent(accessToken: String, eventId: String) {
        eventViewModel.getOneParticipation(accessToken, eventId)
    }

    private fun handleResponseRetrieveEvent(response: Response<Event>) {
        val onSuccess = {
            logD(response.status())
            currentEvent = response.body()!!
            binding.btnJoinEvent.text =
                if (participantsId.contains(userId))
                    "Joined!".also { logI("Participant") }
                else
                    "Join".also { logI("Not participant") }
        }

        val onFailure = {
            toast("Something went wrong while retrieving event")
            logW(response.getFullResponse())
            logI(response.getErrorBodyProperties().toString())
        }

        response.resolve(onSuccess, onFailure)
    }
}