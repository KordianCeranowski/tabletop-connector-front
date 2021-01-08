package com.example.tabletop.main.fragment

import android.os.Bundle
import android.view.View
import android.viewbinding.library.activity.viewBinding
import android.viewbinding.library.fragment.viewBinding
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.tabletop.R
import com.example.tabletop.databinding.ActivityEventFormBinding
import com.example.tabletop.databinding.FragmentEventChatBinding
import com.example.tabletop.main.adapter.ChosenGameAdapter
import com.example.tabletop.main.adapter.MessageAdapter
import com.example.tabletop.mvvm.model.Event
import com.example.tabletop.mvvm.model.helpers.EndlessRecyclerViewScrollListener
import com.example.tabletop.mvvm.model.helpers.Message
import com.example.tabletop.mvvm.viewmodel.EventViewModel
import com.example.tabletop.mvvm.viewmodel.UserViewModel
import com.example.tabletop.settings.SettingsManager
import com.example.tabletop.util.*
import dev.ajkueterman.lazyviewmodels.lazyViewModels
import kotlinx.android.synthetic.main.fragment_event_chat.*
import kotlinx.coroutines.runBlocking
import net.alexandroid.utils.mylogkt.logE
import net.alexandroid.utils.mylogkt.logI
import net.alexandroid.utils.mylogkt.logW

class EventChatFragment : BaseFragment(R.layout.fragment_event_chat) {

    override val binding: FragmentEventChatBinding by viewBinding()
    private lateinit var settingsManager: SettingsManager
    private val messageAdapter by lazy { MessageAdapter() }
    private val linearlayoutManager  = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true)


    private var userName = ""
    private var userId = ""
    private var userIdMarker = ""


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.onCreate(savedInstanceState)

        settingsManager = SettingsManager(requireContext())

        val scrollListener = object : EndlessRecyclerViewScrollListener(linearlayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                loadMoreData()
            }
        }
        binding.recyclerView.apply {
            layoutManager = linearlayoutManager
            adapter = messageAdapter
            addOnScrollListener(scrollListener)
        }

        binding.button.setOnClickListener{
            handleButton()
        }

        userIdMarker = messageAdapter.userIdMarker
        val id = "284166ef-b4ca-4b34-9fa6-c6a102d59f22"
        getProfileIdAndSetData(listOf(getMockMessage(), getMockMessage(), getMockMessage(), getMockMessage(),  getMockMessage(),getMockMessage(), getMockMessage(), getMockMessage(), getMockMessage(),  getMockMessage()))
    }

    private fun handleButton() {
        if (binding.message.text.toString().trim() != "") {
            val message = Message("You", binding.message.text.toString(), "")
            messageAdapter.addDataOnTop(message, binding.recyclerView)
            binding.message.setText("")
        }
    }

    private fun getProfileIdAndSetData(messages: List<Message>){
        val userViewModel = UserViewModel()
        userViewModel.getMyProfile(getAccessToken(requireContext()))
        userViewModel.responseGetProfile.observe(viewLifecycleOwner) {
            val onSuccess = {
                if (it.body() != null) {
                    val profile = it.body()!!
                    this.userId = profile.id
                    this.userName = profile.fullName

                    logI("userId: $userId")

                    addMessages(messages)

                } else {
                    logW("Response body is empty")
                }
            }
            val onFailure = {
                logE("Couldn't access profile info")
            }
            it.resolve(onSuccess, onFailure)
        }
    }

    private fun addMessages(messages: List<Message>) {
        val newMessages = mutableListOf<Message>()
        messages.forEach {
            if(it.handle == this.userId){
                it.handle = this.userIdMarker
            }
            newMessages.add(it)
        }

        messageAdapter.addData(newMessages, binding.recyclerView)
    }

    fun loadMoreData(){
        logI("loading")
        val id = "284166ef-b4ca-4b34-9fa6-c6a102d59f22"
        addMessages(listOf(getMockMessage(id),getMockMessage(),getMockMessage(id),getMockMessage(),getMockMessage(id),getMockMessage(),getMockMessage(id),getMockMessage(),getMockMessage(id),getMockMessage()))
        logI(messageAdapter.getData().toString())
    }

}