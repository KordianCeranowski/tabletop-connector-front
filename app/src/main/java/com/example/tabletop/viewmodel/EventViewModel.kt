package com.example.tabletop.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tabletop.model.Event
import com.example.tabletop.repository.*
import kotlinx.coroutines.launch
import retrofit2.Response
import kotlin.reflect.KFunction

class EventViewModel(private val repository: EventRepository) : ViewModel() {

    val responseOne = MutableLiveData<Response<Event>>()

    val responseMany = MutableLiveData<Response<List<Event>>>()

    // private fun <T> saveEventAndExecute(
    //     request: KFunction<Unit>,
    //     event: T,
    //     args: List<Any> = emptyList(),
    //     action: (List<Any>) -> Unit
    // ) {
    //
    // }
    //
    // init {
    //     repository::getAll
    // }
    //
    // fun testGetAll() {
    //
    // }

    fun getAll() {
        viewModelScope.launch {
            responseMany.value = repository.getAll()
        }
    }

    fun getMany(sort: String, order: String) {
        viewModelScope.launch {
            responseMany.value = repository.getMany(sort, order)
        }
    }

    fun getMany(options: Map<String, String>) {
        viewModelScope.launch {
            responseMany.value = repository.getMany(options)
        }
    }

    fun save(event: Event) {
        viewModelScope.launch {
            responseOne.value = repository.save(event)
        }
    }

    fun getOne(id: String) {
        viewModelScope.launch {
            responseOne.value = repository.getOne(id)
        }
    }

    fun remove(id: String) {
        viewModelScope.launch {
            responseOne.value = repository.remove(id)
        }
    }

    fun edit(id: String, newEvent: Event) {
        viewModelScope.launch {
            responseOne.value = repository.edit(id, newEvent)
        }
    }
}