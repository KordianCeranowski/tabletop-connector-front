package com.example.tabletop.utils

import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tabletop.repository.PostRepository
import com.example.tabletop.repository.Repository
import com.example.tabletop.repository.UserRepository
import com.example.tabletop.viewModels.ViewModelFactory
import com.example.tabletop.viewModels.PostViewModel
import com.example.tabletop.viewModels.UserViewModel

object Helpers {
    fun AppCompatActivity.justStartActivity(activity: AppCompatActivity) {
        startActivity(Intent(this, activity::class.java))
    }

    fun AppCompatActivity.viewModelOf(repository: Repository): ViewModel {
        val viewModel = when (repository) {
            is UserRepository -> UserViewModel(repository)
            is PostRepository -> PostViewModel(repository)
            else -> throw Exception("Invalid repository")
        }
        return ViewModelProvider(this, ViewModelFactory(repository))
            .get(viewModel::class.java)
    }

    fun AppCompatActivity.logIt(msg: Any?, tag: String = this.getClassName()) {
        Log.d(tag, msg.toString())
    }

    fun AppCompatActivity.logIt(vararg msgs: Any?, tag: String = this.getClassName()) {
        msgs.forEach {
            Log.d(tag, it.toString())
        }
    }

    fun Any.getClassName(): String = this::class.simpleName as String
}