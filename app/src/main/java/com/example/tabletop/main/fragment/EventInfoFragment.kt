package com.example.tabletop.main.fragment

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.example.tabletop.R
import com.example.tabletop.databinding.FragmentEventInfoBinding
import com.example.tabletop.main.activity.MainActivity
import com.example.tabletop.mvvm.model.Event
import com.example.tabletop.mvvm.model.helpers.LoginResponse
import com.example.tabletop.mvvm.viewmodel.EventViewModel
import com.example.tabletop.settings.SettingsManager
import com.example.tabletop.util.className
import kotlinx.coroutines.launch
import net.alexandroid.utils.mylogkt.logI
import retrofit2.Response
import splitties.activities.start
import splitties.toast.toast

class EventInfoFragment : BaseFragment(R.layout.fragment_event_info) {

    override val binding: FragmentEventInfoBinding by viewBinding()

    private lateinit var settingsManager: SettingsManager

    fun setup() {
        settingsManager = SettingsManager(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()

        logI("Created ${this.className}")

        val event = arguments?.getSerializable("EVENT") as Event

        binding.tvEventName.text = event.name
        binding.tvEventCreator.text = event.creator.username
        binding.tvEventDate.text = event.date

        binding.btnJoinEvent.setOnClickListener {
            settingsManager
                .userAccessTokenFlow
                .asLiveData().observe(viewLifecycleOwner) { authToken ->
                    EventViewModel.run {
                        joinOrLeaveEvent(authToken, event.id)
                        responseJoinOrLeaveEvent.observe(viewLifecycleOwner) { handleResponse(it) }
                    }
                }
        }
    }

    private fun handleResponse(response: Response<Unit>) {
        response.let {
            if (it.isSuccessful) {
                handleSuccess(it)
            } else {
                handleError(it)
            }
        }
    }

    private fun handleSuccess(response: Response<Unit>) {
        toast("Joined/left an event")
    }

    private fun handleError(response: Response<Unit>) {
        toast("Something went wrong")
    }
}