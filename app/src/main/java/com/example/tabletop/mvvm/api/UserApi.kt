package com.example.tabletop.mvvm.api

import com.example.tabletop.mvvm.model.User
import com.example.tabletop.mvvm.model.helpers.*
import com.example.tabletop.util.USER_API_ENDPOINT
import com.example.tabletop.util.USER_API_LOGIN_ENDPOINT
import com.example.tabletop.util.USER_API_REGISTER_ENDPOINT
import retrofit2.Response
import retrofit2.http.*

interface UserApi {
    @POST(USER_API_LOGIN_ENDPOINT)
    suspend fun login(
        @Body loginForm: LoginForm
    ): Response<LoginResponse>

    @POST(USER_API_REGISTER_ENDPOINT)
    suspend fun register(
        @Body registerRequest: RegisterRequest
    ): Response<RegisterResponse>

    @GET(USER_API_ENDPOINT)
    suspend fun getMany(
        @Query("_sort") sort: String,
        @Query("_order") order: String
    ): Response<Many<User>>

    @GET(USER_API_ENDPOINT)
    suspend fun getMany(
        @QueryMap options: Map<String, String>
    ): Response<Many<User>>

    @GET("$USER_API_ENDPOINT{id}")
    suspend fun getOne(
        @Path("id") id: String
    ): Response<User>

    @DELETE("$USER_API_ENDPOINT{id}")
    suspend fun remove(
        @Path("id") id: String
    ): Response<User>

    @PUT("$USER_API_ENDPOINT{id}")
    suspend fun edit(
        @Path("id") id: String,
        @Body user: User
    ): Response<User>
}