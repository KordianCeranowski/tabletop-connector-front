package com.example.tabletop.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tabletop.repository.*

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {

    override fun <A : ViewModel?> create(modelClass: Class<A>): A {
        return when (repository) {
            is UserRepository -> UserViewModel(repository)
            is EventRepository -> EventViewModel(repository)
            is MockRepository -> MockViewModel(repository)
        } as A
    }
}