package com.example.tabletop.activity

import android.os.Bundle
import android.viewbinding.library.activity.viewBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tabletop.adapter.EventAdapter
import com.example.tabletop.databinding.ActivityMainBinding
import com.example.tabletop.model.Event
import com.example.tabletop.repository.EventRepository
import com.example.tabletop.util.Helpers.getRandomDate
import com.example.tabletop.util.times
import com.example.tabletop.viewmodel.EventViewModel
import dev.ajkueterman.lazyviewmodels.lazyViewModels
import kotlinx.android.synthetic.main.activity_main.*
import net.alexandroid.utils.mylogkt.logI
import splitties.toast.UnreliableToastApi
import java.util.*
import kotlin.text.StringBuilder

@UnreliableToastApi
class MainActivity : BaseActivity() {

    override val binding: ActivityMainBinding by viewBinding()

    //private val mockViewModel: MockViewModel by lazyViewModels { MockViewModel(MockRepository) }

    //private val mockAdapter by lazy { MockAdapter() }

    private val eventViewModel: EventViewModel by lazyViewModels { EventViewModel(EventRepository) }

    private val eventAdapter by lazy { EventAdapter() }

    override fun setup() {
        //mockViewModel = viewModelOf(MockRepository) as MockViewModel

        // //RECYCLER VIEW - MOCK
        // binding.recyclerView.apply {
        //     layoutManager = LinearLayoutManager(this@MainActivity)
        //     adapter = mockAdapter
        // }

        //RECYCLER VIEW - EVENT
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = eventAdapter
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setup()

        val events = List(10) { idx ->
            Event("Name $idx",
                "Creator $idx",
                getRandomDate(),
                emptyList()
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
        /*
        mockViewModel.getMany(2,"id","desc")

        mockViewModel.responseMany.observe(this) { response ->
            if (response.isSuccessful) {
                response.body()?.let { mockAdapter.setData(it) }
            } else {
               toast(response.code())
            }
        }
        */
        /*
        mockViewModel.getPost()

        mockViewModel.responseOne.observe(this, { response ->
            if (response.isSuccessful) {
                response.body()?.run {
                    logIt(
                        "User ID: $userId",
                        "Title: $title",
                        "Body: $body"
                    )
                    // testTextView.text = title
                }
            } else {
                logIt(response.errorBody())
                // testTextView.text = response.code().toString()
            }
         })
        */
    }
}