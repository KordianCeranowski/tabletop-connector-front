package com.example.tabletop.mvvm.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.tabletop.mvvm.model.Game
import com.example.tabletop.mvvm.repository.GameRepository
import kotlinx.coroutines.launch
import net.alexandroid.utils.mylogkt.logI

class GameViewModel : ApiViewModel<Game>() {

    fun getMany(accessToken: String, search: String) {
        getMany(accessToken, mapOf(Pair("search", search)))
    }

    fun getMany(accessToken: String, options: Map<String, String> = emptyMap()) {
        logI("getMany $options")
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