package com.example.tabletop.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tabletop.model.Post
import com.example.tabletop.repository.*
import kotlinx.coroutines.launch
import retrofit2.Response

class MockViewModel(private val repository: MockRepository) : ViewModel() {

    val responseSingle = MutableLiveData<Response<Post>>()

    val responseMany = MutableLiveData<Response<List<Post>>>()

    fun getPost() {
        viewModelScope.launch {
            responseSingle.value = repository.getPost()
        }
    }

    fun getCustomPosts(userId: Int, sort: String, order: String) {
        viewModelScope.launch {
            responseMany.value = repository.getCustomPosts(userId, sort, order)
        }
    }
}