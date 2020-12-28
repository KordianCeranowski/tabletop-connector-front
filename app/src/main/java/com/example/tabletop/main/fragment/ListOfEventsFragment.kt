package com.example.tabletop.main.fragment

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tabletop.R
import com.example.tabletop.databinding.FragmentListOfEventsBinding
import com.example.tabletop.main.activity.EventFormActivity
import com.example.tabletop.main.adapter.EventAdapter
import com.example.tabletop.mvvm.model.Event
import com.example.tabletop.mvvm.model.helpers.Many
import com.example.tabletop.mvvm.viewmodel.EventViewModel
import com.example.tabletop.settings.SettingsManager
import com.example.tabletop.util.*
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import net.alexandroid.utils.mylogkt.logD
import net.alexandroid.utils.mylogkt.logI
import net.alexandroid.utils.mylogkt.logW
import retrofit2.Response
import splitties.fragments.start
import splitties.toast.UnreliableToastApi
import splitties.toast.toast

@Suppress("COMPATIBILITY_WARNING")
@UnreliableToastApi
class ListOfEventsFragment : BaseFragment(R.layout.fragment_list_of_events) {

    override val binding: FragmentListOfEventsBinding by viewBinding()

    private lateinit var settingsManager: SettingsManager

    private val eventAdapter by lazy { EventAdapter() }

    fun setup() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = eventAdapter
        }
        settingsManager = SettingsManager(requireContext())
        //logI("Starting ${this.className}")
    }

    private fun setupOnClickListeners() {
        binding.btnCreateEvent.setOnClickListener {
            start<EventFormActivity>()
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()
        setupOnClickListeners()

        lifecycleScope.launch {
            settingsManager
                .userAccessTokenFlow
                .asLiveData()
                .observe(viewLifecycleOwner) { retrieveEvents(it) }
        }
    }

    private fun retrieveEvents(accessToken: String) {
        var isAlreadyHandled = false
        EventViewModel.run {
            getMany(accessToken)
            responseMany.observe(viewLifecycleOwner) {
                if (!(isAlreadyHandled)) {
                    isAlreadyHandled = true
                    handleResponse(it)
                }
            }
        }
    }

    private fun handleResponse(response: Response<Many<Event>>) {

        val onSuccess = {
            logD(response.status())
            response.body()?.let { eventAdapter.setData(it.results) } as Unit
        }

        val onFailure = {
            logW(response.getFullResponse())
            logW(response.getErrorBodyProperties().toString())
            toast("Could not retrieve events")
            // if (!(this@LoginActivity::errorBodyProperties.isInitialized)) {
            //     errorBodyProperties = response.getErrorBodyProperties()
            // }
            // logE(response.getFullResponse())
            // logD(errorBodyProperties.toString())
            //
            // val key = "detail"
            // val value = "No active account found with the given credentials"
            //
            // if (errorBodyProperties[key] == value) {
            //     toast("Invalid credentials")
            // } else {
            //     toast("Something went wrong")
            // }
        }

        response.resolve(onSuccess, onFailure)
    }
}
