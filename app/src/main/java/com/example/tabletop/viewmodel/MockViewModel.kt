package com.example.tabletop.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tabletop.model.Post
import com.example.tabletop.repository.MockRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class MockViewModel(private val repository: MockRepository) : ViewModel() {

    val response = MutableLiveData<Response<Post>>()

    fun getPost() {
        viewModelScope.launch {
            response.value = repository.getPost()
        }
    }
}