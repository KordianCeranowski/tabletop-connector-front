package com.example.tabletop.mvvm.api

import com.example.tabletop.mvvm.model.Game
import com.example.tabletop.mvvm.model.helpers.Many
import com.example.tabletop.util.GAME_API_ENDPOINT
import retrofit2.Response
import retrofit2.http.*

interface GameApi {
    @GET(GAME_API_ENDPOINT)
    suspend fun getMany(
        @Query("_sort") sort: String,
        @Query("_order") order: String
    ): Response<Many<Game>>

    @GET(GAME_API_ENDPOINT)
    suspend fun getMany(
        @Header("Authorization") auth: String,
        @QueryMap options: Map<String, String>
    ): Response<Many<Game>>

    @POST(GAME_API_ENDPOINT)
    suspend fun save(
        @Header("Authorization") auth: String,
        @Body game: Game
    ): Response<Game>

    @GET("$GAME_API_ENDPOINT{id}")
    suspend fun getOne(
        @Path("id") id: String
    ): Response<Game>

    @DELETE("$GAME_API_ENDPOINT{id}")
    suspend fun remove(
        @Path("id") id: String
    ): Response<Game>

    @PUT("$GAME_API_ENDPOINT{id}")
    suspend fun edit(
        @Path("id") id: String,
        @Body event: Game
    ): Response<Game>
}