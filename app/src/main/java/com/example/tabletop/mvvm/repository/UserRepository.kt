package com.example.tabletop.mvvm.repository

import com.example.tabletop.mvvm.api.RetrofitInstance.userApi
import com.example.tabletop.mvvm.model.User
import com.example.tabletop.mvvm.model.helpers.*
import retrofit2.Response

object UserRepository : Repository<User>() {

    override suspend fun getMany(sort: String, order: String): Response<Many<User>> {
        return userApi.getMany(sort, order)
    }

    override suspend fun getMany(accessToken: String, options: Map<String, String>): Response<Many<User>> {
        return userApi.getMany(accessToken, options)
    }

    override suspend fun getOne(id: String): Response<User> {
        return userApi.getOne(id)
    }

    override suspend fun remove(id: String): Response<User> {
        return userApi.remove(id)
    }

    override suspend fun edit(id: String, model: User): Response<User> {
        return userApi.edit(id, model)
    }

    suspend fun login(loginRequest: LoginRequest): Response<LoginResponse> {
        return userApi.login(loginRequest)
    }

    suspend fun register(registerRequest: RegisterRequest): Response<User> {
        return userApi.register(registerRequest)
    }

    suspend fun getProfile(accessToken: String): Response<Profile> {
        return userApi.getProfile(accessToken)
    }

    suspend fun editProfile(accessToken: String, id: String, profile: Profile) : Response<Profile>{
        return userApi.editProfile(accessToken, id, profile)
    }

    suspend fun getNewAccessToken(refreshToken: RefreshRequest): Response<RefreshResponse> {
        return userApi.getNewAccessToken(refreshToken)
    }
}