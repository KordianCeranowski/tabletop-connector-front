package com.example.tabletop.api

import com.example.tabletop.model.Post
import retrofit2.http.GET

interface SimpleApi {
    @GET("posts/1")
    suspend fun getPost(): Post
}