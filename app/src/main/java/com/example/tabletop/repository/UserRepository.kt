package com.example.tabletop.repository

import com.example.tabletop.api.RetrofitInstance.userApi
import com.example.tabletop.model.LoginRequest
import com.example.tabletop.model.RegisterRequest
import com.example.tabletop.model.User
import retrofit2.Response

class UserRepository : Repository() {
    suspend fun getCustomUsers(sort: String, order: String): Response<List<User>> {
        return userApi.getCustomUsers(sort, order)
    }

    suspend fun getCustomUsers(options: Map<String, String>): Response<List<User>> {
        return userApi.getCustomUsers(options)
    }

    suspend fun register(registerRequest: RegisterRequest): Response<Triple<String, String, String>> {
        return userApi.register(registerRequest)
    }

    suspend fun login(loginRequest: LoginRequest): Response<User> {
        return userApi.login(loginRequest)
    }

    suspend fun getUser(id: String): Response<User> {
        return userApi.getUser(id)
    }

    suspend fun remove(id: String): Response<User> {
        return userApi.remove(id)
    }

    suspend fun edit(id: String, user: User): Response<User> {
        return userApi.edit(id, user)
    }
}
