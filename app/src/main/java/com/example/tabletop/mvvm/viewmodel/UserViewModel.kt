package com.example.tabletop.mvvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.tabletop.mvvm.model.User
import com.example.tabletop.mvvm.model.helpers.*
import com.example.tabletop.mvvm.repository.UserRepository
import kotlinx.coroutines.launch
import retrofit2.Response

object UserViewModel : BaseViewModel<User>() {

    val responseLogin = MutableLiveData<Response<LoginResponse>>()

    val responseRegister = MutableLiveData<Response<RegisterResponse>>()

    fun login(loginForm: LoginForm) {
        viewModelScope.launch {
            responseLogin.value = UserRepository.login(loginForm)
        }
    }

    fun register(registerRequest: RegisterRequest) {
        viewModelScope.launch {
            responseRegister.value = UserRepository.register(registerRequest)
        }
    }

    override fun getMany(sort: String, order: String) {
        viewModelScope.launch {
            responseMany.value = UserRepository.getMany(sort, order)
        }
    }

    override fun getMany(auth: String, options: Map<String, String>) {
        viewModelScope.launch {
            responseMany.value = UserRepository.getMany(auth, options)
        }
    }

    override fun getOne(id: String) {
        viewModelScope.launch {
            responseOne.value = UserRepository.getOne(id)
        }
    }

    override fun remove(id: String) {
        viewModelScope.launch {
            responseOne.value = UserRepository.remove(id)
        }
    }

    override fun edit(id: String, newModel: User) {
        viewModelScope.launch {
            responseOne.value = UserRepository.edit(id, newModel)
        }
    }
}