package com.example.tabletop.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.tabletop.model.User
import com.example.tabletop.repository.*
import com.example.tabletop.model.helpers.LoginRequest
import kotlinx.coroutines.launch

class UserViewModel(private val repository: UserRepository) : BaseViewModel<User>() {

    override fun save(model: User) {
        viewModelScope.launch {
            responseOne.value = repository.save(model)
        }
    }

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

    override fun edit(id: String, newModel: User) {
        viewModelScope.launch {
            responseOne.value = repository.edit(id, newModel)
        }
    }

    fun login(loginRequest: LoginRequest) {
        viewModelScope.launch {
            responseOne.value = repository.login(loginRequest)
        }
    }
}