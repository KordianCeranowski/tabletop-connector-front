package com.example.tabletop.mvvm.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.tabletop.mvvm.model.Game
import com.example.tabletop.mvvm.repository.GameRepository
import kotlinx.coroutines.launch
import net.alexandroid.utils.mylogkt.logI

class GameViewModel(var lastSearch: String = "") : ApiViewModel<Game>() {

    fun getNextPage(accessToken: String, nextPage: String){
        viewModelScope.launch {
            responseManyNext.value = GameRepository.getMany(accessToken, mapOf(Pair("page", nextPage), Pair("search", lastSearch)))
        }
    }

    fun getMany(accessToken: String, search: String) {
        lastSearch = search
        viewModelScope.launch {
            responseMany.value = GameRepository.getMany(accessToken, mapOf(Pair("search", search)))
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