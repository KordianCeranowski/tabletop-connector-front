package com.example.tabletop.mvvm.api

import com.example.tabletop.mvvm.model.Game
import com.example.tabletop.mvvm.model.helpers.Many
import com.example.tabletop.util.GAME_API_ENDPOINT
import retrofit2.Response
import retrofit2.http.*

interface GameApi {

    @GET(GAME_API_ENDPOINT)
    suspend fun getMany(
        @Header("Authorization") auth: String,
        @QueryMap options: Map<String, String>
    ): Response<Many<Game>>

    @GET("$GAME_API_ENDPOINT{id}/")
    suspend fun getOne(
        @Header("Authorization") auth: String,
        @Path("id") id: String
    ): Response<Game>

    @POST(GAME_API_ENDPOINT)
    suspend fun save(
        @Header("Authorization") auth: String,
        @Body game: Game
    ): Response<Game>
}