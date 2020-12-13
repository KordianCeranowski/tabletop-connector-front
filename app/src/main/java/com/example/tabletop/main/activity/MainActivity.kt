package com.example.tabletop.main.activity

import android.os.Bundle
import android.viewbinding.library.activity.viewBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tabletop.main.adapter.EventAdapter
import com.example.tabletop.databinding.ActivityMainBinding
import com.example.tabletop.mvvm.model.Event
import com.example.tabletop.mvvm.model.Game
import com.example.tabletop.mvvm.repository.EventRepository
import com.example.tabletop.util.Helpers.getMockAddress
import com.example.tabletop.util.Helpers.getRandomDate
import com.example.tabletop.mvvm.viewmodel.EventViewModel
import com.example.tabletop.util.random
import dev.ajkueterman.lazyviewmodels.lazyViewModels
import kotlinx.android.synthetic.main.activity_main.*
import splitties.toast.UnreliableToastApi
import java.util.*

@UnreliableToastApi
class MainActivity : BaseActivity() {

    override val binding: ActivityMainBinding by viewBinding()

    private val eventViewModel: EventViewModel by lazyViewModels { EventViewModel(EventRepository) }

    private val eventAdapter by lazy { EventAdapter() }

    override fun setup() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = eventAdapter
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setup()

        val events = List(10) { idx ->
            Event("Event ${idx + 1}",
                "Creator ${idx + 1}",
                getRandomDate(),
                getMockAddress(),
                emptyList(),
                List(10) {
                    Game(
                        "Name ${it + 1}",
                        "URI",
                        2,
                        (3..8).random(),
                        (15..90 step 5).random()
                    )
                }
            )
        }

        eventAdapter.setData(events)

        /*
        eventViewModel.save(getMockEvent())

        eventViewModel.responseOne.observe(this, { response ->
            if (response.isSuccessful) {
                logD(response.getFullResponse())
                if (response.code() == 201){
                    //działa
                } else {
                    //nie działa
                }
            } else {
                logE(response.getFullResponse())
            }
        })
        */

        /*
        eventViewModel.getMany("id","desc")

        eventViewModel.responseMany.observe(this) { response ->
           if (response.isSuccessful) {
               response.body()?.let { eventAdapter.setData(it) }
           } else {
              toast(response.code())
           }
       }
        */
    }
}