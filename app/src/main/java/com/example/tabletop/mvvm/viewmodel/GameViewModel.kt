package com.example.tabletop.mvvm.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.tabletop.mvvm.model.Game
import com.example.tabletop.mvvm.repository.GameRepository
import kotlinx.coroutines.launch

object GameViewModel : BaseViewModel<Game>(), IViewModelSave<Game> {

    override fun getMany(accessToken: String, options: Map<String, String>) {
        viewModelScope.launch {
            responseMany.value = GameRepository.getMany(accessToken, options)
        }
    }

    override fun save(accessToken: String, model: Game) {
        viewModelScope.launch {
            responseOne.value = GameRepository.save(accessToken, model)
        }
    }

    override fun getOne(accessToken: String, id: String) {
        viewModelScope.launch {
            responseOne.value = GameRepository.getOne(accessToken, id)
        }
    }

    override fun remove(accessToken: String, id: String) {
        viewModelScope.launch {
            responseOne.value = GameRepository.remove(accessToken, id)
        }
    }

    override fun edit(accessToken: String, id: String, newModel: Game) {
        viewModelScope.launch {
            responseOne.value = GameRepository.edit(accessToken, id, newModel)
        }
    }
}