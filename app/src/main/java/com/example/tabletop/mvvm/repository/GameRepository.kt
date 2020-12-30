package com.example.tabletop.mvvm.repository

import com.example.tabletop.mvvm.api.RetrofitInstance.gameApi
import com.example.tabletop.mvvm.model.Game
import com.example.tabletop.mvvm.model.helpers.Many
import retrofit2.Response

object GameRepository : Repository<Game>(), IRepositorySave<Game> {

    override suspend fun getMany(accessToken: String, options: Map<String, String>): Response<Many<Game>> {
        return gameApi.getMany(accessToken, options)
    }

    override suspend fun save(accessToken: String, model: Game): Response<Game> {
        return gameApi.save(accessToken, model)
    }

    override suspend fun getOne(accessToken: String, id: String): Response<Game> {
        return gameApi.getOne(id)
    }

    override suspend fun remove(accessToken: String, id: String): Response<Game> {
        return gameApi.remove(id)
    }

    override suspend fun edit(accessToken: String, id: String, model: Game): Response<Game> {
        return gameApi.edit(id, model)
    }
}