package com.example.tabletop.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.tabletop.model.Game
import com.example.tabletop.repository.GameRepository
import kotlinx.coroutines.launch

class GameViewModel(private val repository: GameRepository) : BaseViewModel<Game>() {

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

    override fun save(model: Game) {
        viewModelScope.launch {
            responseOne.value = repository.save(model)
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

    override fun edit(id: String, newModel: Game) {
        viewModelScope.launch {
            responseOne.value = repository.edit(id, newModel)
        }
    }
}