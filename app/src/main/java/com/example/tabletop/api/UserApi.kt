package com.example.tabletop.api

import com.example.tabletop.model.User
import com.example.tabletop.util.Constants.USER_API_ENDPOINT
import com.example.tabletop.util.Constants.USER_API_LOGIN_ENDPOINT
import com.example.tabletop.util.Constants.USER_API_REGISTER_ENDPOINT
import com.example.tabletop.util.LoginRequest
import com.example.tabletop.util.RegisterRequest
import retrofit2.Response
import retrofit2.http.*

interface UserApi {
    @GET(USER_API_ENDPOINT)
    suspend fun getMany(
        @Query("_sort") sort: String,
        @Query("_order") order: String
    ): Response<List<User>>

    @GET(USER_API_ENDPOINT)
    suspend fun getMany(
        @QueryMap options: Map<String, String>
    ): Response<List<User>>

    @POST(USER_API_REGISTER_ENDPOINT)
    suspend fun register(
        @Body registerRequest: RegisterRequest
    ): Response<Triple<String, String, String>>

    @Headers("Authorization: test")
    @POST(USER_API_LOGIN_ENDPOINT)
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): Response<User>

    @GET("$USER_API_ENDPOINT/{id}")
    suspend fun getOne(
        @Path("id") id: String
    ): Response<User>

    @DELETE("$USER_API_ENDPOINT/{id}")
    suspend fun remove(
        @Path("id") id: String
    ): Response<User>

    @PUT("$USER_API_ENDPOINT/{id}")
    suspend fun edit(
        @Path("id") id: String,
        @Body user: User
    ): Response<User>
}