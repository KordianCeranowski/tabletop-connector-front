package com.example.tabletop.repository

import com.example.tabletop.api.RetrofitInstance
import com.example.tabletop.model.Post
import com.example.tabletop.model.User
import retrofit2.Response

class PostRepository : Repository() {
    suspend fun getPost(): Response<Post> = RetrofitInstance.simpleApi.getPost()
}