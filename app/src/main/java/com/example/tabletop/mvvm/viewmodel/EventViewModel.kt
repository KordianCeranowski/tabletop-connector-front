package com.example.tabletop.mvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.tabletop.mvvm.model.Event
import com.example.tabletop.mvvm.model.Game
import com.example.tabletop.mvvm.repository.*
import com.example.tabletop.util.SingleLiveEvent
import kotlinx.coroutines.launch
import retrofit2.Response

object EventViewModel : BaseViewModel<Event>(), IViewModelSave<Event> {

    val responseJoinOrLeaveEvent = SingleLiveEvent<Response<Unit>>()

    fun joinOrLeaveEvent(accessToken: String, id: String) {
        viewModelScope.launch {
            responseJoinOrLeaveEvent.value = EventRepository.joinOrLeaveEvent(accessToken, id)
        }
    }

    override fun getMany(accessToken: String, options: Map<String, String>) {
        viewModelScope.launch {
            responseMany.value = EventRepository.getMany(accessToken, options)
        }
    }

    override fun save(accessToken: String, model: Event) {
        viewModelScope.launch {
            responseOne.value = EventRepository.save(accessToken, model)
        }
    }

    override fun getOne(accessToken: String, id: String) {
        viewModelScope.launch {
            responseOne.value = EventRepository.getOne(accessToken, id)
        }
    }

    override fun remove(accessToken: String, id: String) {
        viewModelScope.launch {
            responseOne.value = EventRepository.remove(accessToken, id)
        }
    }

    override fun edit(accessToken: String, id: String, newModel: Event) {
        viewModelScope.launch {
            responseOne.value = EventRepository.edit(accessToken, id, newModel)
        }
    }
}