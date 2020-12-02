package com.example.tabletop.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tabletop.adapter.EventAdapter
import com.example.tabletop.databinding.ActivityMainBinding
import com.example.tabletop.model.Address
import com.example.tabletop.model.Event
import com.example.tabletop.repository.EventRepository
import com.example.tabletop.util.Helpers.getFullResponse
import com.example.tabletop.util.Helpers.gson
import com.example.tabletop.util.Helpers.viewModelOf
import com.example.tabletop.util.runLoggingConfig
import com.example.tabletop.viewmodel.EventViewModel
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import net.alexandroid.utils.mylogkt.logD
import net.alexandroid.utils.mylogkt.logE
import splitties.toast.UnreliableToastApi
import java.util.*

@UnreliableToastApi
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    //private lateinit var mockViewModel: MockViewModel

    //private val mockAdapter by lazy { MockAdapter() }

    private lateinit var eventViewModel: EventViewModel

    private val eventAdapter by lazy { EventAdapter() }
    
    private fun setup() {
        runLoggingConfig()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        eventViewModel = viewModelOf(EventRepository) as EventViewModel

        val newEvent = Event(
            "TEST 1",
            "c7e5510a-ff73-4a7a-adf6-d82239acdf0f",
            "2020-12-10T16:01:00+0000",
            Address(
                "xd",
                "xd",
                "xd",
                "xd",
                "xd",
                21.37,
                12.35
            ),
            null
        )

        eventViewModel.save(newEvent)

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