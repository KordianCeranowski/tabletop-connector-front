package com.example.tabletop.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tabletop.model.Event
import com.example.tabletop.repository.*
import kotlinx.coroutines.launch
import retrofit2.Response

class EventViewModel(private val repository: EventRepository) : ViewModel() {

    val responseSingle = MutableLiveData<Response<Event>>()

    val responseMany = MutableLiveData<Response<List<Event>>>()

    fun getCustomUsers(sort: String, order: String) {
        viewModelScope.launch {
            responseMany.value = repository.getCustomUsers(sort, order)
        }
    }

    fun getCustomUsers(options: Map<String, String>) {
        viewModelScope.launch {
            responseMany.value = repository.getCustomUsers(options)
        }
    }

    fun save(event: Event) {
        viewModelScope.launch {
            responseSingle.value = repository.save(event)
        }
    }

    fun getUser(id: String) {
        viewModelScope.launch {
            responseSingle.value = repository.getEvent(id)
        }
    }

    fun remove(id: String) {
        viewModelScope.launch {
            responseSingle.value = repository.remove(id)
        }
    }

    fun edit(id: String, event: Event) {
        viewModelScope.launch {
            responseSingle.value = repository.edit(id, event)
        }
    }
}