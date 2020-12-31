package com.example.tabletop.mvvm.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.tabletop.mvvm.model.Event
import com.example.tabletop.mvvm.model.helpers.request.EventRequest
import com.example.tabletop.mvvm.repository.*
import com.example.tabletop.util.SingleLiveEvent
import kotlinx.coroutines.launch
import retrofit2.Response

class EventViewModel : BaseViewModel<Event>() {

    val responseParticipation = SingleLiveEvent<Response<Unit>>()

    val responseOneParticipation = SingleLiveEvent<Response<Event>>()

    fun participateInEvent(accessToken: String, id: String) {
        viewModelScope.launch {
            responseParticipation.value = EventRepository.participateInEvent(accessToken, id)
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

    fun getOneParticipation(accessToken: String, id: String) {
        viewModelScope.launch {
            responseOneParticipation.value = EventRepository.getOne(accessToken, id)
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