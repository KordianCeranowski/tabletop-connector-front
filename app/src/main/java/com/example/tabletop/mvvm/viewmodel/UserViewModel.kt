package com.example.tabletop.mvvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.tabletop.mvvm.model.User
import com.example.tabletop.mvvm.model.helpers.*
import com.example.tabletop.mvvm.repository.UserRepository
import kotlinx.coroutines.launch
import net.alexandroid.utils.mylogkt.logI
import retrofit2.Response

@Suppress("ObjectPropertyName")
object UserViewModel : BaseViewModel<User>() {

    private val _responseLogin = MutableLiveData<Response<LoginResponse>>()

    private val _responseRegister = MutableLiveData<Response<RegisterResponse>>()

    val responseLogin: LiveData<Response<LoginResponse>>
        get() = _responseLogin

    val responseRegister: LiveData<Response<RegisterResponse>>
        get() = _responseRegister

    val responseGetProfile = MutableLiveData<Response<Profile>>()
    val responseCreateProfile = MutableLiveData<Response<Profile>>()

    fun login(loginForm: LoginForm) {
        viewModelScope.launch {
            _responseLogin.value = UserRepository.login(loginForm)
        }
    }

    fun register(registerRequest: RegisterRequest) {
        viewModelScope.launch {
            _responseRegister.value = UserRepository.register(registerRequest)
        }
    }

    fun getProfile(auth: String) {
        viewModelScope.launch {
            responseGetProfile.value = UserRepository.getProfile(auth)
        }
    }

    fun editProfile(auth: String, profile: Profile) {
        logI("Editing")
        logI(profile.toString())
        viewModelScope.launch {
            responseCreateProfile.value = UserRepository.editProfile(auth, profile.id, profile)
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