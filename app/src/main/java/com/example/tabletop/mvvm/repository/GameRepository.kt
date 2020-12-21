package com.example.tabletop.mvvm.repository

import com.example.tabletop.mvvm.api.RetrofitInstance.gameApi
import com.example.tabletop.mvvm.model.Game
import com.example.tabletop.mvvm.model.helpers.Many
import retrofit2.Response

object GameRepository : Repository<Game>(), IRepositorySave<Game> {
    override suspend fun getMany(sort: String, order: String): Response<Many<Game>> {
        return gameApi.getMany(sort, order)
    }

    override suspend fun getMany(options: Map<String, String>): Response<Many<Game>> {
        return gameApi.getMany(options)
    }

    override suspend fun save(auth: String, model: Game): Response<Game> {
        return gameApi.save(auth, model)
    }

    override suspend fun getOne(id: String): Response<Game> {
        return gameApi.getOne(id)
    }

    override suspend fun remove(id: String): Response<Game> {
        return gameApi.remove(id)
    }

    override suspend fun edit(id: String, model: Game): Response<Game> {
        return gameApi.edit(id, model)
    }
}