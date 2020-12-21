package com.example.tabletop.mvvm.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.tabletop.mvvm.model.Event
import com.example.tabletop.mvvm.model.Game
import com.example.tabletop.mvvm.repository.*
import kotlinx.coroutines.launch

class EventViewModel(private val repository: EventRepository)
    : BaseViewModel<Event>(), IViewModelSave<Event> {

    override fun getMany(sort: String, order: String) {
        viewModelScope.launch {
            responseMany.value = repository.getMany(sort, order)
        }
    }

    override fun getMany(options: Map<String, String>) {
        viewModelScope.launch {
            responseMany.value = repository.getMany(options)
        }
    }

    override fun save(auth: String, model: Event) {
        viewModelScope.launch {
            responseOne.value = repository.save(auth, model)
        }
    }

    override fun getOne(id: String) {
        viewModelScope.launch {
            responseOne.value = repository.getOne(id)
        }
    }

    override fun remove(id: String) {
        viewModelScope.launch {
            responseOne.value = repository.remove(id)
        }
    }

    override fun edit(id: String, newModel: Event) {
        viewModelScope.launch {
            responseOne.value = repository.edit(id, newModel)
        }
    }
}