package com.example.tabletop.main.fragment

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tabletop.R
import com.example.tabletop.databinding.FragmentListOfEventsBinding
import com.example.tabletop.main.activity.EventFormActivity
import com.example.tabletop.main.adapter.EventAdapter
import com.example.tabletop.mvvm.model.Event
import com.example.tabletop.mvvm.model.helpers.EventWrapper
import com.example.tabletop.mvvm.model.helpers.Many
import com.example.tabletop.mvvm.viewmodel.EventViewModel
import com.example.tabletop.settings.SettingsManager
import com.example.tabletop.util.*
import dev.ajkueterman.lazyviewmodels.lazyActivityViewModels
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import net.alexandroid.utils.mylogkt.logD
import net.alexandroid.utils.mylogkt.logV
import net.alexandroid.utils.mylogkt.logW
import retrofit2.Response
import splitties.fragments.start
import splitties.toast.UnreliableToastApi

@Suppress("COMPATIBILITY_WARNING", "UNCHECKED_CAST")
@UnreliableToastApi
class ListOfEventsFragment : BaseFragment(R.layout.fragment_list_of_events) {

    override val binding: FragmentListOfEventsBinding by viewBinding()

    private val eventViewModel by lazyActivityViewModels { EventViewModel() }

    private lateinit var settingsManager: SettingsManager

    private val eventAdapter by lazy { EventAdapter() }

    private fun setup() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = eventAdapter
            addItemDecoration(
                DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            )
        }
        settingsManager = SettingsManager(requireContext())
    }

    private fun setupBtnOnClickListener() {
        binding.btnCreateEvent.setOnClickListener {
            start<EventFormActivity>()
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()
        setupBtnOnClickListener()

        attachObserverResponseMany()
    }

    override fun onResume() {
        super.onResume()
        val accessToken = runBlocking { settingsManager.userAccessTokenFlow.first() }

        retrieveEvents(accessToken)
    }

    private fun retrieveEvents(accessToken: String) {
        val queryMap =
            (arguments?.getSerializable(Extra.QUERY_MAP()) ?: (emptyMap<Query, String>().also {
                logV("No query map received")
            })) as Map<Query, String>

        eventViewModel.getManyCustom(accessToken, queryMap)
    }

    private fun attachObserverResponseMany() {
        eventViewModel.responseMany.observe(viewLifecycleOwner) { handleResponse(it) }
    }

    private fun handleResponse(response: Response<Many<Event>>) {
        val onSuccess = {
            logD(response.status())
            if (response.body() == null) {
                logW("Response has empty body")
            }
            val events = response.body()?.results ?: emptyList()
            if (events.isEmpty()) {
                binding.tvEmptyList.text = getString(R.string.empty_recycler_view)
            } else {
                var longitude = runBlocking { settingsManager.longitudeFlow.first() }
                var latitude = runBlocking { settingsManager.latitudeFlow.first() }

                if (longitude == 0.0 || latitude == 0.0) {
                    val (_longitude, _latitude) = getCurrentLocation()
                    longitude = _longitude
                    latitude = _latitude
                }

                response.body()?.let {
                    val wrappedEvents = it.results.map { event ->
                        EventWrapper(event, event.getDistanceInMetersFrom(longitude, latitude))
                    }
                    eventAdapter.setData(wrappedEvents)
                } as Unit
            }
        }

        val onFailure = {
            logW(response.getFullResponse())
            logW(response.getErrorJson().toString())
        }

        response.resolve(onSuccess, onFailure)
    }
}
