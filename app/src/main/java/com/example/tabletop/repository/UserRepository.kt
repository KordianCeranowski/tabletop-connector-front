package com.example.tabletop.repository

import com.example.tabletop.api.RetrofitInstance.userApi
import com.example.tabletop.model.User
import com.example.tabletop.util.LoginRequest
import com.example.tabletop.util.RegisterRequest
import retrofit2.Response

object UserRepository : Repository<User>() {

    override suspend fun getMany(sort: String, order: String): Response<List<User>> {
        return userApi.getMany(sort, order)
    }

    override suspend fun getMany(options: Map<String, String>): Response<List<User>> {
        return userApi.getMany(options)
    }

    override suspend fun save(model: User): Response<User> {
        return userApi.save(model)
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

    suspend fun login(loginRequest: LoginRequest): Response<User> {
        return userApi.login(loginRequest)
    }
}