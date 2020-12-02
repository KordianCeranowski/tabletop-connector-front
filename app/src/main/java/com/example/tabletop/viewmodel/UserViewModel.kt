package com.example.tabletop.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tabletop.model.User
import com.example.tabletop.repository.*
import com.example.tabletop.util.LoginRequest
import com.example.tabletop.util.RegisterRequest
import kotlinx.coroutines.launch
import retrofit2.Response

class UserViewModel(private val repository: UserRepository) : ViewModel() {

    val responseOne = MutableLiveData<Response<User>>()

    val responseMany = MutableLiveData<Response<List<User>>>()

    val responseRegister = MutableLiveData<Response<Triple<String, String, String>>>()

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

    fun register(registerRequest: RegisterRequest) {
        val launch = viewModelScope.launch {
            responseRegister.value = repository.register(registerRequest)
        }
    }

    fun login(loginRequest: LoginRequest) {
        viewModelScope.launch {
            responseOne.value = repository.login(loginRequest)
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

    fun edit(id: String, user: User) {
        viewModelScope.launch {
            responseOne.value = repository.edit(id, user)
        }
    }
}