package com.example.tabletop.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tabletop.model.User
import com.example.tabletop.repository.UserRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class UserViewModel(private val repository: UserRepository) : ViewModel() {
    val myResponse = MutableLiveData<Response<User>>()

    fun getUsers() {
        viewModelScope.launch {
            val response = repository.getUsers()
            myResponse.value = response
        }
    }
}