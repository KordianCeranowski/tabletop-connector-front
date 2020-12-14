package com.example.tabletop.main.fragment

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tabletop.R
import com.example.tabletop.databinding.FragmentListOfEventsBinding
import com.example.tabletop.main.activity.EventFormActivity
import com.example.tabletop.main.adapter.EventAdapter
import com.example.tabletop.mvvm.model.Event
import com.example.tabletop.mvvm.repository.EventRepository
import com.example.tabletop.mvvm.viewmodel.EventViewModel
import com.example.tabletop.util.Helpers.className
import com.example.tabletop.util.Helpers.getFullResponse
import com.example.tabletop.util.Helpers.logIt
import dev.ajkueterman.lazyviewmodels.lazyActivityViewModels
import dev.ajkueterman.lazyviewmodels.lazyViewModels
import net.alexandroid.utils.mylogkt.logD
import net.alexandroid.utils.mylogkt.logI
import splitties.fragments.start
import splitties.toast.UnreliableToastApi
import splitties.toast.toast

@UnreliableToastApi
class ListOfEventsFragment
    : BaseFragment(R.layout.fragment_list_of_events) {

    override val binding: FragmentListOfEventsBinding by viewBinding()

    private val eventAdapter by lazy { EventAdapter() }

    fun setup() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = eventAdapter
        }
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

        logI("Created ${this.className}")



        val events = arguments?.getSerializable("EVENTS") as List<Event>

        logI(events.toString())
        eventAdapter.setData(events)
    }
}
