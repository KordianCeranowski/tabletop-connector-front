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

    val responseSingle = MutableLiveData<Response<User>>()

    val responseRegister = MutableLiveData<Response<Triple<String, String, String>>>()

    val responseMany = MutableLiveData<Response<List<User>>>()

    fun getCustomUsers(sort: String, order: String) {
        viewModelScope.launch {
            responseMany.value = repository.getCustomUsers(sort, order)
        }
    }

    fun getCustomUsers(options: Map<String, String>) {
        viewModelScope.launch {
            responseMany.value = repository.getCustomUsers(options)
        }
    }

    fun register(registerRequest: RegisterRequest) {
        val launch = viewModelScope.launch {
            responseRegister.value = repository.register(registerRequest)
        }
    }

    fun login(loginRequest: LoginRequest) {
        viewModelScope.launch {
            responseSingle.value = repository.login(loginRequest)
        }
    }

    fun getUser(id: String) {
        viewModelScope.launch {
            responseSingle.value = repository.getUser(id)
        }
    }

    fun remove(id: String) {
        viewModelScope.launch {
            responseSingle.value = repository.remove(id)
        }
    }

    fun edit(id: String, user: User) {
        viewModelScope.launch {
            responseSingle.value = repository.edit(id, user)
        }
    }
}