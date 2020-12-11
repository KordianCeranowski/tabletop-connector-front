package com.example.tabletop.activity

import android.os.Bundle
import android.viewbinding.library.activity.viewBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tabletop.adapter.EventAdapter
import com.example.tabletop.databinding.ActivityMainBinding
import com.example.tabletop.model.Address
import com.example.tabletop.model.Event
import com.example.tabletop.repository.EventRepository
import com.example.tabletop.repository.MockRepository
import com.example.tabletop.repository.UserRepository
import com.example.tabletop.util.Helpers.getFullResponse
import com.example.tabletop.viewmodel.EventViewModel
import com.example.tabletop.viewmodel.UserViewModel
import dev.ajkueterman.lazyviewmodels.lazyViewModels
import kotlinx.android.synthetic.main.activity_main.*
import net.alexandroid.utils.mylogkt.logD
import net.alexandroid.utils.mylogkt.logE
import splitties.toast.UnreliableToastApi
import java.util.*

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