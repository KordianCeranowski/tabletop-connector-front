package com.example.tabletop.mvvm.repository

import com.example.tabletop.mvvm.api.RetrofitInstance.userApi
import com.example.tabletop.mvvm.model.User
import com.example.tabletop.mvvm.model.helpers.*
import com.example.tabletop.mvvm.model.helpers.request.LoginRequest
import com.example.tabletop.mvvm.model.helpers.request.RefreshRequest
import com.example.tabletop.mvvm.model.helpers.request.RegisterRequest
import retrofit2.Response

object UserRepository : Repository<User>() {

    override suspend fun getMany(accessToken: String, options: Map<String, String>): Response<Many<User>> {
        return userApi.getMany(accessToken, options)
    }

    override suspend fun getOne(accessToken: String, id: String): Response<User> {
        return userApi.getOne(id)
    }

    override suspend fun remove(accessToken: String, id: String): Response<User> {
        return userApi.remove(id)
    }

    override suspend fun edit(accessToken: String, id: String, model: User): Response<User> {
        return userApi.edit(id, model)
    }

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

}