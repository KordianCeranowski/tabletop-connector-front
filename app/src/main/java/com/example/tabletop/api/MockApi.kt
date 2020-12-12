package com.example.tabletop.api

import com.example.tabletop.model.helpers.Many
import com.example.tabletop.model.helpers.Post
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MockApi {
    @GET("posts/1")
    suspend fun getPost(): Response<Post>

    @GET("posts")
    suspend fun getCustomPosts(
        @Query("userId") userId: Int,
        @Query("_sort") sort: String,
        @Query("_order") order: String
    ): Response<Many<Post>>
}