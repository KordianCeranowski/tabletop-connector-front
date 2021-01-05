package com.example.tabletop.mvvm.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.tabletop.mvvm.model.User
import com.example.tabletop.mvvm.model.helpers.*
import com.example.tabletop.mvvm.model.helpers.request.LoginRequest
import com.example.tabletop.mvvm.model.helpers.request.RegisterRequest
import com.example.tabletop.mvvm.repository.UserRepository
import com.example.tabletop.util.SingleLiveEvent
import com.google.gson.JsonObject
import kotlinx.coroutines.launch
import retrofit2.Response

@Suppress("ObjectPropertyName")
class UserViewModel : ApiViewModel<User>() {

    val responseLogin = SingleLiveEvent<Response<LoginResponse>>()

    val responseGetProfile = SingleLiveEvent<Response<Profile>>()

    val responseEditProfile = SingleLiveEvent<Response<Profile>>()

    val responseLogout = SingleLiveEvent<Response<Unit>>()

    val responseChangeUsername = SingleLiveEvent<Response<JsonObject>>()

    val responseChangePassword  = SingleLiveEvent<Response<JsonObject>>()

    val responseDeleteMyAccount  = SingleLiveEvent<Response<JsonObject>>()

    fun deleteMyAccount(accessToken: String, json: JsonObject) {
        viewModelScope.launch {
            responseDeleteMyAccount.value = UserRepository.deleteMyAccount(accessToken, json)
        }
    }

    fun changeUsername(accessToken: String, json: JsonObject) {
        viewModelScope.launch {
            responseChangeUsername.value = UserRepository.changeUsername(accessToken, json)
        }
    }

    fun changePassword(accessToken: String, json: JsonObject) {
        viewModelScope.launch {
            responseChangePassword.value = UserRepository.changePassword(accessToken, json)
        }
    }

    fun logout(accessToken: String) {
        viewModelScope.launch {
            responseLogout.value = UserRepository.logout(accessToken)
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
                UserRepository.editProfile(accessToken, id, profile)
        }
    }
}