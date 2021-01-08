package com.example.tabletop.main.fragment

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tabletop.R
import com.example.tabletop.databinding.FragmentEventChatBinding
import com.example.tabletop.main.adapter.MessageAdapter
import com.example.tabletop.mvvm.model.helpers.EndlessRecyclerViewScrollListener
import com.example.tabletop.mvvm.model.helpers.Message
import com.example.tabletop.mvvm.viewmodel.UserViewModel
import com.example.tabletop.settings.SettingsManager
import com.example.tabletop.util.*
import net.alexandroid.utils.mylogkt.logE
import net.alexandroid.utils.mylogkt.logI
import net.alexandroid.utils.mylogkt.logW

class EventChatFragment : BaseFragment(R.layout.fragment_event_chat) {

    override val binding: FragmentEventChatBinding by viewBinding()

    private lateinit var settingsManager: SettingsManager

    private val messageAdapter by lazy { MessageAdapter() }

    private val linearLayoutManager =
        LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true)

    private var userId = ""
    private var userIdMarker = ""


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.onCreate(savedInstanceState)

        settingsManager = SettingsManager(requireContext())

        val scrollListener = object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                loadMoreData()
            }
        }

        binding.recyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = messageAdapter
            addOnScrollListener(scrollListener)
        }

        binding.button.setOnClickListener{
            handleButton()
        }

        userIdMarker = messageAdapter.userIdMarker

        getProfileIdAndSetData(
            List(10) { getMockMessage() }
        )
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
            if (it.handle == this.userId){
                it.handle = this.userIdMarker
            }
            newMessages.add(it)
        }

        messageAdapter.addData(newMessages, binding.recyclerView)
    }

    fun loadMoreData(){
        logI("loading")
        val id = "284166ef-b4ca-4b34-9fa6-c6a102d59f22"

        addMessages(List(10) {
            if (it % 2 == 0) getMockMessage(id) else getMockMessage()
        })
        logI(messageAdapter.getData().toString())
    }

}