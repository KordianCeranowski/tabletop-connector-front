package com.example.tabletop.repository

import com.example.tabletop.model.Model
import retrofit2.Response

abstract class Repository<T : Model> {
    abstract suspend fun getMany(sort: String, order: String): Response<List<T>>

    abstract suspend fun getMany(options: Map<String, String>): Response<List<T>>

    abstract suspend fun save(model: T): Response<T>

    abstract suspend fun getOne(id: String): Response<T>

    abstract suspend fun remove(id: String): Response<T>

    abstract suspend fun edit(id: String, model: T): Response<T>
}
