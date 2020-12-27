package com.example.tabletop.mvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.tabletop.mvvm.model.Event
import com.example.tabletop.mvvm.model.Game
import com.example.tabletop.mvvm.repository.*
import kotlinx.coroutines.launch
import retrofit2.Response

object EventViewModel : BaseViewModel<Event>(), IViewModelSave<Event> {

    val responseJoinOrLeaveEvent = MutableLiveData<Response<Unit>>()

    override fun getMany(sort: String, order: String) {
        viewModelScope.launch {
            responseMany.value = EventRepository.getMany(sort, order)
        }
    }

    override fun getMany(auth: String, options: Map<String, String>) {
        viewModelScope.launch {
            responseMany.value = EventRepository.getMany(auth, options)
        }
    }

    override fun save(auth: String, model: Event) {
        viewModelScope.launch {
            responseOne.value = EventRepository.save(auth, model)
        }
    }

    override fun getOne(id: String) {
        viewModelScope.launch {
            responseOne.value = EventRepository.getOne(id)
        }
    }

    override fun remove(id: String) {
        viewModelScope.launch {
            responseOne.value = EventRepository.remove(id)
        }
    }

    override fun edit(id: String, newModel: Event) {
        viewModelScope.launch {
            responseOne.value = EventRepository.edit(id, newModel)
        }
    }

    fun joinOrLeaveEvent(auth: String, id: String) {
        viewModelScope.launch {
            responseJoinOrLeaveEvent.value = EventRepository.joinOrLeaveEvent(auth, id)
        }
    }
}