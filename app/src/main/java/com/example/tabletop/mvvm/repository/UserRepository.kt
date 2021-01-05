package com.example.tabletop.mvvm.repository

import com.example.tabletop.mvvm.api.RetrofitInstance.userApi
import com.example.tabletop.mvvm.model.User
import com.example.tabletop.mvvm.model.helpers.*
import com.example.tabletop.mvvm.model.helpers.request.LoginRequest
import com.example.tabletop.mvvm.model.helpers.request.RefreshRequest
import com.example.tabletop.mvvm.model.helpers.request.RegisterRequest
import com.google.gson.JsonObject
import retrofit2.Response

object UserRepository {

    suspend fun getMyProfile(accessToken: String): Response<Profile> {
        return userApi.getMyProfile(accessToken)
    }

    suspend fun getProfile(accessToken: String, id: String): Response<Profile> {
        return userApi.getProfile(accessToken, id)
    }

    suspend fun editProfile(accessToken: String, id: String, profile: Profile) : Response<Profile>{
        return userApi.editProfile(accessToken, id, profile)
    }

    suspend fun login(loginRequest: LoginRequest): Response<LoginResponse> {
        return userApi.login(loginRequest)
    }

    suspend fun register(registerRequest: RegisterRequest): Response<User> {
        return userApi.register(registerRequest)
    }

    suspend fun logout(accessToken: String): Response<Unit> {
        return userApi.logout(accessToken)
    }

    suspend fun changeUsername(accessToken: String, json: JsonObject): Response<JsonObject> {
        return userApi.changeUsername(accessToken, json)
    }

    suspend fun changePassword(accessToken: String, json: JsonObject): Response<JsonObject> {
        return userApi.changePassword(accessToken, json)
    }

    suspend fun deleteMyAccount(accessToken: String, json: JsonObject): Response<JsonObject> {
        return userApi.deleteMyAccount(accessToken, json)
    }
}