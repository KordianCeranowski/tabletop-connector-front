package com.example.tabletop.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tabletop.model.Post
import com.example.tabletop.repository.PostRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class PostViewModel(private val repository: PostRepository) : ViewModel() {

    val myResponse = MutableLiveData<Response<Post>>()

    fun getPost() {
        viewModelScope.launch {
            myResponse.value = repository.getPost()
        }
    }
}