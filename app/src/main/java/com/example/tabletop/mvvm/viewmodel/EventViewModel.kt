package com.example.tabletop.mvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tabletop.mvvm.model.Event
import com.example.tabletop.mvvm.model.Game
import com.example.tabletop.mvvm.model.User
import com.example.tabletop.mvvm.model.helpers.Many
import com.example.tabletop.mvvm.model.helpers.request.EventRequest
import com.example.tabletop.mvvm.repository.*
import com.example.tabletop.util.SingleLiveEvent
import kotlinx.coroutines.launch
import retrofit2.Response

object EventViewModel : BaseViewModel<Event>() {

    val responseJoinOrLeaveEvent = SingleLiveEvent<Response<Unit>>()

    fun joinOrLeaveEvent(accessToken: String, id: String) {
        viewModelScope.launch {
            responseJoinOrLeaveEvent.value = EventRepository.joinOrLeaveEvent(accessToken, id)
        }
    }

    fun getMany(accessToken: String, options: Map<String, String> = emptyMap()) {
        viewModelScope.launch {
            responseMany.value = EventRepository.getMany(accessToken, options)
        }
    }

    fun save(accessToken: String, eventRequest: EventRequest) {
        viewModelScope.launch {
            responseOne.value = EventRepository.save(accessToken, eventRequest)
        }
    }

    fun getOne(accessToken: String, id: String) {
        viewModelScope.launch {
            responseOne.value = EventRepository.getOne(accessToken, id)
        }
    }

    fun remove(accessToken: String, id: String) {
        viewModelScope.launch {
            responseOne.value = EventRepository.remove(accessToken, id)
        }
    }

    fun edit(accessToken: String, id: String, newModel: Event) {
        viewModelScope.launch {
            responseOne.value = EventRepository.edit(accessToken, id, newModel)
        }
    }
}