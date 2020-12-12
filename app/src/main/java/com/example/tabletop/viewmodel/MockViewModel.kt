package com.example.tabletop.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.tabletop.model.helpers.Post
import com.example.tabletop.repository.*
import kotlinx.coroutines.launch

class MockViewModel(private val repository: MockRepository) : BaseViewModel<Post>() {

    fun getPost() {
        viewModelScope.launch {
            responseOne.value = repository.getOne()
        }
    }

    fun getCustomPosts(userId: Int, sort: String, order: String) {
        viewModelScope.launch {
            responseMany.value = repository.getMany(userId, sort, order)
        }
    }

    override fun getMany(sort: String, order: String) {
        TODO("Not yet implemented")
    }

    override fun getMany(options: Map<String, String>) {
        TODO("Not yet implemented")
    }

    override fun save(model: Post) {
        TODO("Not yet implemented")
    }

    override fun getOne(id: String) {
        TODO("Not yet implemented")
    }

    override fun remove(id: String) {
        TODO("Not yet implemented")
    }

    override fun edit(id: String, newModel: Post) {
        TODO("Not yet implemented")
    }
}