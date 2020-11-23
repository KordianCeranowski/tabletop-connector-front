package com.example.tabletop.repository

import com.example.tabletop.api.RetrofitInstance
import com.example.tabletop.model.LoginRequest
import com.example.tabletop.model.RegisterRequest
import com.example.tabletop.model.User
import retrofit2.Response

class UserRepository : Repository() {
    suspend fun getCustomUsers(sort: String, order: String): Response<List<User>> {
        return RetrofitInstance.userApi.getCustomUsers(sort, order)
    }

    suspend fun getCustomUsers(options: Map<String, String>): Response<List<User>> {
        return RetrofitInstance.userApi.getCustomUsers(options)
    }

    suspend fun save(registerRequest: RegisterRequest): Response<User> {
        return RetrofitInstance.userApi.save(registerRequest)
    }

    suspend fun login(loginRequest: LoginRequest): Response<User> {
        return RetrofitInstance.userApi.login(loginRequest)
    }

    suspend fun getUser(id: Int): Response<User> = RetrofitInstance.userApi.getUser(id)

    suspend fun remove(id: Int): Response<User> = RetrofitInstance.userApi.remove(id)

    suspend fun edit(id: Int, user: User): Response<User> = RetrofitInstance.userApi.edit(id, user)
}
