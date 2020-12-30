package com.example.tabletop.mvvm.api

import com.example.tabletop.mvvm.model.User
import com.example.tabletop.mvvm.model.helpers.*
import com.example.tabletop.mvvm.model.helpers.request.LoginRequest
import com.example.tabletop.mvvm.model.helpers.request.RefreshRequest
import com.example.tabletop.mvvm.model.helpers.request.RegisterRequest
import com.example.tabletop.util.*
import retrofit2.Response
import retrofit2.http.*

interface UserApi {

    // USER
    @GET(USER_API_ENDPOINT)
    suspend fun getMany(
        @Header("Authorization") auth: String,
        @QueryMap options: Map<String, String>
    ): Response<Many<User>>

    @GET("$USER_API_ENDPOINT{id}/")
    suspend fun getOne(
        @Path("id") id: String
    ): Response<User>

    @DELETE("$USER_API_ENDPOINT{id}/")
    suspend fun remove(
        @Path("id") id: String
    ): Response<User>

    @PATCH("$USER_API_ENDPOINT{id}/")
    suspend fun edit(
        @Path("id") id: String,
        @Body user: User
    ): Response<User>

    @POST(USER_API_ENDPOINT_LOGIN)
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): Response<LoginResponse>

    @POST(USER_API_ENDPOINT_REGISTER)
    suspend fun register(
        @Body registerRequest: RegisterRequest
    ): Response<User>

    @POST(USER_API_ENDPOINT_LOGOUT)
    suspend fun logout(
        @Header("Authorization") auth: String,
    ): Response<Unit>

    // PROFILE
    @GET(USER_API_ENDPOINT_MY_PROFILE)
    suspend fun getMyProfile(
        @Header("Authorization") accessToken: String
    ): Response<Profile>

    @GET("$USER_API_ENDPOINT_PROFILE{id}/")
    suspend fun getProfile(
        @Header("Authorization") accessToken: String,
        @Path("id") id: String
    ): Response<Profile>

    @PATCH("$USER_API_ENDPOINT_CREATE_PROFILE{id}/")
    suspend fun editProfile(
        @Header("Authorization") auth: String,
        @Path("id") id: String,
        @Body profile: Profile
    ): Response<Profile>
}