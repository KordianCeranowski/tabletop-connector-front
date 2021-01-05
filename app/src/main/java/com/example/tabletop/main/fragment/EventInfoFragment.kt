package com.example.tabletop.main.fragment

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.lifecycle.observe
import com.example.tabletop.R
import com.example.tabletop.databinding.FragmentEventInfoBinding
import com.example.tabletop.main.activity.ProfileActivity
import com.example.tabletop.mvvm.model.Event
import com.example.tabletop.mvvm.viewmodel.BottomNavBarViewModel
import com.example.tabletop.mvvm.viewmodel.EventViewModel
import com.example.tabletop.settings.SettingsManager
import com.example.tabletop.util.*
import com.livinglifetechway.k4kotlin.core.androidx.shortToast
import dev.ajkueterman.lazyviewmodels.lazyActivityViewModels
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import net.alexandroid.utils.mylogkt.logD
import net.alexandroid.utils.mylogkt.logI
import net.alexandroid.utils.mylogkt.logW
import retrofit2.Response
import splitties.toast.UnreliableToastApi
import splitties.toast.toast

@Suppress("COMPATIBILITY_WARNING")
@UnreliableToastApi
class EventInfoFragment : BaseFragment(R.layout.fragment_event_info) {

    override val binding: FragmentEventInfoBinding by viewBinding()

    private val eventViewModel by lazyActivityViewModels { EventViewModel() }

    private val bottomNavBarViewModel by lazyActivityViewModels { BottomNavBarViewModel() }

    private lateinit var settingsManager: SettingsManager

    private lateinit var currentEvent: Event

    private val accessToken: String
        get() = runBlocking { settingsManager.userAccessTokenFlow.first() }

    private val userId: String
        get() = runBlocking { settingsManager.userIdFlow.first() }

    private val participantsId: List<String>
        get() = currentEvent.participants.map { it.id }

    fun setup() {
        settingsManager = SettingsManager(requireContext())
        currentEvent = arguments?.getSerializable(Extra.EVENT.toString()) as Event
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()

        attachObservers()

        setupOnClickListeners()
    }

    override fun onResume() {
        super.onResume()

        val (date, time) = getSeparatedDateTime(currentEvent.date)

        binding.tvEventName.text = currentEvent.name
        binding.tvEventCreator.text = currentEvent.creator.profile.fullName
        binding.tvEventDate.text = date
        binding.tvEventTime.text = time

        setupBtnJoinEventVisibility()
        setBtnJoinEventIcon()

        setChatAble()
    }

    private fun setupBtnJoinEventVisibility() {
        binding.btnJoinEvent.let { btn ->
            if (userId == currentEvent.creator.id) {
                btn.visibility = View.INVISIBLE
            } else {
                btn.visibility = View.VISIBLE
            }
        }
    }

    private fun setBtnJoinEventIcon(showToast: Boolean = false) {
        val isParticipant = participantsId.contains(userId)
        val icon =
            if (isParticipant)
                R.drawable.ic_check.also { if (showToast) shortToast("Joined event!") }
            else
                R.drawable.ic_plus.also { if (showToast) shortToast("Left event!") }

        binding.btnJoinEvent.setImageResource(icon)
    }

    private fun setChatAble() {
        val isChatEnabled = participantsId.contains(userId)
        bottomNavBarViewModel.setChatEnabled(isChatEnabled)
    }

    private fun setupOnClickListeners() {
        binding.btnJoinEvent.setOnClickListener {
            val accessToken = runBlocking { settingsManager.userAccessTokenFlow.first() }
            participateInEvent(accessToken, currentEvent.id)
            setBtnJoinEventIcon()
        }

        binding.tvEventCreator.setOnClickListener {
            context?.startWithExtra<ProfileActivity>(
                Extra.PROFILE_ID.toString() to currentEvent.creator.profile.id
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
            logI(response.getErrorJson().toString())
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
            setBtnJoinEventIcon(true)
        }

        val onFailure = {
            toast("Something went wrong while retrieving event")
            logW(response.getFullResponse())
            logI(response.getErrorJson().toString())
        }

        response.resolve(onSuccess, onFailure)
    }
}