package com.example.tabletop.mvvm.repository

import retrofit2.Response

interface IRepositorySave<T> {
    suspend fun save(accessToken: String, model: T): Response<T>
}