package com.example.tabletop.main.fragment

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
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

    private lateinit var settingsManager: SettingsManager

    fun setup() {
        settingsManager = SettingsManager(requireContext())
        //logI("Starting ${this.className}")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()

        val passedEvent = arguments?.getSerializable(EXTRA_EVENT) as Event

        binding.tvEventName.text = passedEvent.name
        binding.tvEventCreator.text = passedEvent.creator.username
        binding.tvEventDate.text = passedEvent.date

        attachObserver()

        lifecycleScope.launch {
            val userId = settingsManager.userIdFlow.first()
            if (userId == passedEvent.creator.id) {
                binding.btnJoinEvent.visibility = View.INVISIBLE
            } else {
                binding.btnJoinEvent.setOnClickListener {
                    lifecycleScope.launch {
                        val accessToken = settingsManager.userAccessTokenFlow.first()
                        joinOrLeaveEvent(accessToken, passedEvent.id)
                    }
                }
            }
        }

        binding.tvEventCreator.setOnClickListener {
            context?.startWithExtra<ProfileActivity>(
                EXTRA_PROFILE_ID to passedEvent.creator.profile.id
            )
        }
    }

    private fun attachObserver() {
        EventViewModel.responseJoinOrLeaveEvent.observe(viewLifecycleOwner) { handleResponse(it) }
    }

    private fun joinOrLeaveEvent(accessToken: String, eventId: String) {
        EventViewModel.joinOrLeaveEvent(accessToken, eventId)
    }

    private fun handleResponse(response: Response<Unit>) {
        val onSuccess = {
            toast("Joined/left an event")
        }

        val onFailure = {
            toast("Something went wrong")
            logW(response.getFullResponse())
            logI(response.getErrorBodyProperties().toString())

        }

        response.resolve(onSuccess, onFailure)
    }
}