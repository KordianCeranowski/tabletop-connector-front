package com.example.tabletop.mvvm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tabletop.mvvm.model.Game
import com.example.tabletop.mvvm.model.User
import com.example.tabletop.mvvm.model.helpers.Many
import com.example.tabletop.mvvm.repository.GameRepository
import com.example.tabletop.util.SingleLiveEvent
import kotlinx.coroutines.launch
import retrofit2.Response

object GameViewModel : BaseViewModel<Game>() {

    fun getMany(accessToken: String, options: Map<String, String>) {
        viewModelScope.launch {
            responseMany.value = GameRepository.getMany(accessToken, options)
        }
    }

    fun getOne(accessToken: String, id: String) {
        viewModelScope.launch {
            responseOne.value = GameRepository.getOne(accessToken, id)
        }
    }

    fun save(accessToken: String, model: Game) {
        viewModelScope.launch {
            responseOne.value = GameRepository.save(accessToken, model)
        }
    }

}