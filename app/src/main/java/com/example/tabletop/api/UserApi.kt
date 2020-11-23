package com.example.tabletop.api

import com.example.tabletop.model.LoginRequest
import com.example.tabletop.model.RegisterRequest
import com.example.tabletop.model.User
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface UserApi {
    @GET("user")
    suspend fun getCustomUsers(
        @Query("_sort") sort: String,
        @Query("_order") order: String
    ): Response<List<User>>

    @GET("user")
    suspend fun getCustomUsers(
        @QueryMap options: Map<String, String>
    ): Response<List<User>>

    @POST("user")
    suspend fun save(
        @Body registerRequest: RegisterRequest
    ): Response<User>

    @POST("login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): Response<User>

    @GET("user/{id}")
    suspend fun getUser(
        @Path("id") id: Int
    ): Response<User>

    @DELETE("user/{id}")
    suspend fun remove(
        @Path("id") id: Int
    ): Response<User>

    @PUT("user/{id}")
    suspend fun edit(
        @Path("id") id: Int,
        @Body user: User
    ): Response<User>
}