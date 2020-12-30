package com.example.tabletop.mvvm.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.tabletop.mvvm.model.User
import com.example.tabletop.mvvm.model.helpers.*
import com.example.tabletop.mvvm.repository.UserRepository
import com.example.tabletop.util.SingleLiveEvent
import kotlinx.coroutines.launch
import net.alexandroid.utils.mylogkt.logI
import retrofit2.Response

@Suppress("ObjectPropertyName")
object UserViewModel : BaseViewModel<User>() {

    val responseLogin = SingleLiveEvent<Response<LoginResponse>>()

    val responseGetProfile = SingleLiveEvent<Response<Profile>>()

    val responseEditProfile = SingleLiveEvent<Response<Profile>>()

    val responseAccessToken = SingleLiveEvent<Response<RefreshResponse>>()

    fun getNewAccessToken(refreshRequest: RefreshRequest) {
        viewModelScope.launch {
            responseAccessToken.value = UserRepository.getNewAccessToken(refreshRequest)
        }
    }

    fun login(loginRequest: LoginRequest) {
        viewModelScope.launch {
            responseLogin.value = UserRepository.login(loginRequest)
        }
    }

    fun register(registerRequest: RegisterRequest) {
        viewModelScope.launch {
            responseOne.value = UserRepository.register(registerRequest)
        }
    }

    fun getMyProfile(accessToken: String) {
        viewModelScope.launch {
            responseGetProfile.value = UserRepository.getMyProfile(accessToken)
        }
    }

    fun getProfile(accessToken: String, id: String) {
        viewModelScope.launch {
            responseGetProfile.value = UserRepository.getProfile(accessToken, id)
        }
    }

    fun editProfile(accessToken: String, id: String, profile: Profile) {
        viewModelScope.launch {
            responseEditProfile.value =
                UserRepository.editProfile(accessToken, profile.id, profile)
        }
    }

    override fun getMany(accessToken: String, options: Map<String, String>) {
        viewModelScope.launch {
            responseMany.value = UserRepository.getMany(accessToken, options)
        }
    }

    override fun getOne(accessToken: String, id: String) {
        viewModelScope.launch {
            responseOne.value = UserRepository.getOne(accessToken, id)
        }
    }

    override fun remove(accessToken: String, id: String) {
        viewModelScope.launch {
            responseOne.value = UserRepository.remove(accessToken, id)
        }
    }

    override fun edit(accessToken: String, id: String, newModel: User) {
        viewModelScope.launch {
            responseOne.value = UserRepository.edit(accessToken, id, newModel)
        }
    }
}