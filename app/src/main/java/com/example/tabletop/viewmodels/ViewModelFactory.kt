package com.example.tabletop.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tabletop.repository.PostRepository
import com.example.tabletop.repository.Repository
import com.example.tabletop.repository.UserRepository

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when (repository) {
            is UserRepository -> UserViewModel(repository)
            is PostRepository -> PostViewModel(repository)
            else -> throw Exception("Invalid repository")
        } as T
    }
}