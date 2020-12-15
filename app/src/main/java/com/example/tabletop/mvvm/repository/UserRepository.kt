package com.example.tabletop.mvvm.repository

import com.example.tabletop.mvvm.api.RetrofitInstance.userApi
import com.example.tabletop.mvvm.model.User
import com.example.tabletop.mvvm.model.helpers.*
import retrofit2.Response

object UserRepository : Repository<User>() {

    suspend fun login(loginForm: LoginForm): Response<LoginResponse> {
        return userApi.login(loginForm)
    }

    suspend fun register(user: RegisterRequest): Response<RegisterResponse> {
        return userApi.register(user)
    }

    override suspend fun getMany(sort: String, order: String): Response<Many<User>> {
        return userApi.getMany(sort, order)
    }

    override suspend fun getMany(options: Map<String, String>): Response<Many<User>> {
        return userApi.getMany(options)
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
}