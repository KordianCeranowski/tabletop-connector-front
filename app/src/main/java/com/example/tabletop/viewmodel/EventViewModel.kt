package com.example.tabletop.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tabletop.model.Event
import com.example.tabletop.repository.*
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

    fun getUser(id: String) {
        viewModelScope.launch {
            respEvent.value = repository.getEvent(id)
        }
    }

    fun remove(id: String) {
        viewModelScope.launch {
            respEvent.value = repository.remove(id)
        }
    }

    fun edit(id: String, event: Event) {
        viewModelScope.launch {
            respEvent.value = repository.edit(id, event)
        }
    }
}