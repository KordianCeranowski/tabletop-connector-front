package com.example.tabletop.mvvm.api

import com.example.tabletop.mvvm.model.User
import com.example.tabletop.mvvm.model.helpers.*
import com.example.tabletop.mvvm.model.helpers.request.LoginRequest
import com.example.tabletop.mvvm.model.helpers.request.RefreshRequest
import com.example.tabletop.mvvm.model.helpers.request.RegisterRequest
import com.example.tabletop.util.*
import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.*

interface UserApi {

    // USER
    @DELETE("$USER_API_ENDPOINT{id}/")
    suspend fun remove(
        @Path("id") id: String
    ): Response<User>

    @POST(USER_API_ENDPOINT_SET_USERNAME)
    suspend fun changeUsername(
        @Header("Authorization") accessToken: String,
        @Body json: JsonObject
    ): Response<JsonObject>

    @POST(USER_API_ENDPOINT_SET_PASSWORD)
    suspend fun changePassword(
        @Header("Authorization") accessToken: String,
        @Body json: JsonObject
    ): Response<JsonObject>

    @POST(USER_API_ENDPOINT_REGISTER)
    suspend fun register(
        @Body registerRequest: RegisterRequest
    ): Response<User>

    @POST(USER_API_ENDPOINT_LOGIN)
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): Response<LoginResponse>

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
        @Header("Authorization") accessToken: String,
        @Path("id") id: String,
        @Body profile: Profile
    ): Response<Profile>

    //@DELETE(USER_API_ENDPOINT_DELETE_MY_ACCOUNT)
    @HTTP(method = "DELETE", path = USER_API_ENDPOINT_DELETE_MY_ACCOUNT, hasBody = true)
    suspend fun deleteMyAccount(
        @Header("Authorization") accessToken: String,
        @Body json: JsonObject
    ): Response<JsonObject>
}