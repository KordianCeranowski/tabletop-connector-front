package com.example.tabletop.mvvm.repository

import com.example.tabletop.mvvm.model.Model
import com.example.tabletop.mvvm.model.helpers.Many
import retrofit2.Response

abstract class Repository<T : Model> {

    abstract suspend fun getMany(
        accessToken: String,
        options: Map<String, String>
    ): Response<Many<T>>

    abstract suspend fun getOne(accessToken: String, id: String): Response<T>

    abstract suspend fun remove(accessToken: String, id: String): Response<T>

    abstract suspend fun edit(accessToken: String, id: String, model: T): Response<T>
}
