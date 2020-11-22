package com.example.tabletop.repository

import com.example.tabletop.api.RetrofitInstance
import com.example.tabletop.model.User
import retrofit2.Response

class UserRepository : Repository() {
    suspend fun getUsers(): Response<User> = RetrofitInstance.userApi.getUsers()
}
