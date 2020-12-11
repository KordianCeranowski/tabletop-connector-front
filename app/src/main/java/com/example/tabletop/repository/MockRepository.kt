package com.example.tabletop.repository

import com.example.tabletop.api.RetrofitInstance
import com.example.tabletop.model.helpers.Post
import retrofit2.Response

object MockRepository : Repository<Post>() {
    suspend fun getOne(): Response<Post> {
        return RetrofitInstance.mockApi.getPost()
    }

    suspend fun getMany(userId: Int, sort: String, order: String): Response<List<Post>> {
        return RetrofitInstance.mockApi.getCustomPosts(userId, sort, order)
    }

    override suspend fun getMany(sort: String, order: String): Response<List<Post>> {
        TODO("Not yet implemented")
    }

    override suspend fun getMany(options: Map<String, String>): Response<List<Post>> {
        TODO("Not yet implemented")
    }

    override suspend fun save(model: Post): Response<Post> {
        TODO("Not yet implemented")
    }

    override suspend fun getOne(id: String): Response<Post> {
        TODO("Not yet implemented")
    }

    override suspend fun remove(id: String): Response<Post> {
        TODO("Not yet implemented")
    }

    override suspend fun edit(id: String, model: Post): Response<Post> {
        TODO("Not yet implemented")
    }
}