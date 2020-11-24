package com.example.tabletop.repository

import com.example.tabletop.api.RetrofitInstance
import com.example.tabletop.model.Post
import retrofit2.Response

class MockRepository : Repository() {
    suspend fun getPost(): Response<Post> = RetrofitInstance.mockApi.getPost()
}