package com.example.tabletop.repository

import com.example.tabletop.api.RetrofitInstance.userApi
import com.example.tabletop.model.User
import com.example.tabletop.util.LoginRequest
import com.example.tabletop.util.RegisterRequest
import retrofit2.Response

suspend fun UserRepository.getMany(sort: String, order: String): Response<List<User>> {
    return userApi.getMany(sort, order)
}

suspend fun UserRepository.getMany(options: Map<String, String>): Response<List<User>> {
    return userApi.getMany(options)
}

suspend fun UserRepository.register(
    registerRequest: RegisterRequest
): Response<Triple<String, String, String>> {
    return userApi.register(registerRequest)
}

suspend fun UserRepository.login(loginRequest: LoginRequest): Response<User> {
    return userApi.login(loginRequest)
}

suspend fun UserRepository.getOne(id: String): Response<User> {
    return userApi.getOne(id)
}

suspend fun UserRepository.remove(id: String): Response<User> {
    return userApi.remove(id)
}

suspend fun UserRepository.edit(id: String, user: User): Response<User> {
    return userApi.edit(id, user)
}