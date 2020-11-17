package com.example.tabletop.repository

import com.example.tabletop.api.RetrofitInstance
import com.example.tabletop.model.Post

class Repository {
    suspend fun getPost(): Post = RetrofitInstance.api.getPost()
}