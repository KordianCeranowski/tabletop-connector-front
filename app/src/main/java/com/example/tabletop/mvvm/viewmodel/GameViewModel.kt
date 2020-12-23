package com.example.tabletop.mvvm.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.tabletop.mvvm.model.Game
import com.example.tabletop.mvvm.repository.GameRepository
import kotlinx.coroutines.launch

object GameViewModel : BaseViewModel<Game>(), IViewModelSave<Game> {

    override fun getMany(sort: String, order: String) {
        viewModelScope.launch {
            responseMany.value = GameRepository.getMany(sort, order)
        }
    }

    override fun getMany(auth: String, options: Map<String, String>) {
        viewModelScope.launch {
            responseMany.value = GameRepository.getMany(auth, options)
        }
    }

    override fun save(auth: String, model: Game) {
        viewModelScope.launch {
            responseOne.value = GameRepository.save(auth, model)
        }
    }

    override fun getOne(id: String) {
        viewModelScope.launch {
            responseOne.value = GameRepository.getOne(id)
        }
    }

    override fun remove(id: String) {
        viewModelScope.launch {
            responseOne.value = GameRepository.remove(id)
        }
    }

    override fun edit(id: String, newModel: Game) {
        viewModelScope.launch {
            responseOne.value = GameRepository.edit(id, newModel)
        }
    }
}