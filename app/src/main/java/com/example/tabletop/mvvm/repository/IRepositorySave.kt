package com.example.tabletop.mvvm.repository

import retrofit2.Response

interface IRepositorySave<T> {
    suspend fun save(auth: String, model: T): Response<T>
}