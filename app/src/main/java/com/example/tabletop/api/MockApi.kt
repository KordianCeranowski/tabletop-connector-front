package com.example.tabletop.api

import com.example.tabletop.model.Post
import retrofit2.Response
import retrofit2.http.GET

interface MockApi {
    @GET("posts/1")
    suspend fun getPost(): Response<Post>
}