package com.example.tabletop.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tabletop.model.LoginRequest
import com.example.tabletop.model.RegisterRequest
import com.example.tabletop.model.User
import com.example.tabletop.repository.UserRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class UserViewModel(private val repository: UserRepository) : ViewModel() {

    val respUser = MutableLiveData<Response<User>>()

    val respListOfUsers = MutableLiveData<Response<List<User>>>()

    fun getUser(userId: Int) {
        viewModelScope.launch {
            respUser.value = repository.getUser(userId)
        }
    }

    fun getCustomUsers(sort: String, order: String) {
        viewModelScope.launch {
            respListOfUsers.value = repository.getCustomUsers(sort, order)
        }
    }

    fun getCustomUsers(options: Map<String, String>) {
        viewModelScope.launch {
            respListOfUsers.value = repository.getCustomUsers(options)
        }
    }

    fun register(registerRequest: RegisterRequest) {
        viewModelScope.launch {
            respUser.value = repository.register(registerRequest)
        }
    }

    fun login(loginRequest: LoginRequest) {
        viewModelScope.launch {
            respUser.value = repository.login(loginRequest)
        }
    }
}