package com.example.tabletop.api

import com.example.tabletop.model.User
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface UserApi {
    @GET("user")
    suspend fun getUsers(): Response<User>

    @GET("user/{id}")
    suspend fun getUsers(@Path("id") id: Int): Response<User>

    @FormUrlEncoded
    @POST("user")
    suspend fun getUsers(@Body newUser: User): Response<User>
}