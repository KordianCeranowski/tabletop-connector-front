package com.example.tabletop.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tabletop.model.Event
import com.example.tabletop.model.User
import com.example.tabletop.repository.EventRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class EventViewModel(private val repository: EventRepository) : ViewModel() {

    val respEvent = MutableLiveData<Response<Event>>()

    val respListOfEvents = MutableLiveData<Response<List<Event>>>()

    fun getCustomUsers(sort: String, order: String) {
        viewModelScope.launch {
            respListOfEvents.value = repository.getCustomUsers(sort, order)
        }
    }

    fun getCustomUsers(options: Map<String, String>) {
        viewModelScope.launch {
            respListOfEvents.value = repository.getCustomUsers(options)
        }
    }

    fun save(event: Event) {
        viewModelScope.launch {
            respEvent.value = repository.save(event)
        }
    }

    fun getUser(id: Int) {
        viewModelScope.launch {
            respEvent.value = repository.getEvent(id)
        }
    }

    fun remove(id: Int) {
        viewModelScope.launch {
            respEvent.value = repository.remove(id)
        }
    }

    fun edit(id: Int, event: Event) {
        viewModelScope.launch {
            respEvent.value = repository.edit(id, event)
        }
    }
}