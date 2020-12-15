package com.example.tabletop.mvvm.repository

import retrofit2.Response

interface IRepositorySave<T> {
    suspend fun save(model: T): Response<T>
}