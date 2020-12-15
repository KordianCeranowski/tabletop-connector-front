package com.example.tabletop.mvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.tabletop.mvvm.model.User
import com.example.tabletop.mvvm.model.helpers.*
import com.example.tabletop.mvvm.repository.*
import kotlinx.coroutines.launch
import retrofit2.Response

class UserViewModel(private val repository: UserRepository) : BaseViewModel<User>() {

    val responseLogin = MutableLiveData<Response<LoginResponse>>()

    val responseRegister = MutableLiveData<Response<RegisterResponse>>()

    fun login(loginForm: LoginForm) {
        viewModelScope.launch {
            responseLogin.value = repository.login(loginForm)
        }
    }

    fun register(user: RegisterRequest) {
        viewModelScope.launch {
            responseRegister.value = repository.register(user)
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
}