package com.example.tabletop.mvvm.repository

import com.example.tabletop.mvvm.api.RetrofitInstance.gameApi
import com.example.tabletop.mvvm.model.Game
import com.example.tabletop.mvvm.model.helpers.Many
import retrofit2.Response

object GameRepository {

    suspend fun getMany(accessToken: String, options: Map<String, String>): Response<Many<Game>> {
        return gameApi.getMany(accessToken, options)
    }

    suspend fun save(accessToken: String, model: Game): Response<Game> {
        return gameApi.save(accessToken, model)
    }

    suspend fun getOne(accessToken: String, id: String): Response<Game> {
        return gameApi.getOne(accessToken, id)
    }
}