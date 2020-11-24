package com.example.tabletop.repository

import com.example.tabletop.api.RetrofitInstance.userApi
import com.example.tabletop.model.LoginRequest
import com.example.tabletop.model.RegisterRequest
import com.example.tabletop.model.User
import retrofit2.Response

suspend fun UserRepository.getCustomUsers(sort: String, order: String): Response<List<User>> {
    return userApi.getCustomUsers(sort, order)
}

suspend fun UserRepository.getCustomUsers(options: Map<String, String>): Response<List<User>> {
    return userApi.getCustomUsers(options)
}

suspend fun UserRepository.register(
    registerRequest: RegisterRequest
): Response<Triple<String, String, String>> {
    return userApi.register(registerRequest)
}

suspend fun UserRepository.login(loginRequest: LoginRequest): Response<User> {
    return userApi.login(loginRequest)
}

suspend fun UserRepository.getUser(id: String): Response<User> {
    return userApi.getUser(id)
}

suspend fun UserRepository.remove(id: String): Response<User> {
    return userApi.remove(id)
}

suspend fun UserRepository.edit(id: String, user: User): Response<User> {
    return userApi.edit(id, user)
}