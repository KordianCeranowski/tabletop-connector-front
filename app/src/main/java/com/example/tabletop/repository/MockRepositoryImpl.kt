package com.example.tabletop.repository

import com.example.tabletop.api.RetrofitInstance
import com.example.tabletop.model.Post
import retrofit2.Response

suspend fun MockRepository.getPost(): Response<Post> {
    return RetrofitInstance.mockApi.getPost()
}
