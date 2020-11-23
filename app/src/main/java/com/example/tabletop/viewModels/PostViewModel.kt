package com.example.tabletop.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tabletop.model.Post
import com.example.tabletop.repository.PostRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class PostViewModel(private val repository: PostRepository) : ViewModel() {

    val response = MutableLiveData<Response<Post>>()

    fun getPost() {
        viewModelScope.launch {
            response.value = repository.getPost()
        }
    }
}