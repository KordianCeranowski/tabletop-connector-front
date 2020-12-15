package com.example.tabletop.mvvm.repository

import com.example.tabletop.mvvm.model.Model
import com.example.tabletop.mvvm.model.helpers.Many
import retrofit2.Response

abstract class Repository<T : Model> {
    abstract suspend fun getMany(sort: String, order: String): Response<Many<T>>

    abstract suspend fun getMany(options: Map<String, String>): Response<Many<T>>

    abstract suspend fun getOne(id: String): Response<T>

    abstract suspend fun remove(id: String): Response<T>

    abstract suspend fun edit(id: String, model: T): Response<T>
}
