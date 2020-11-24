package com.example.tabletop.util

import android.content.Intent
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tabletop.repository.MockRepository
import com.example.tabletop.repository.Repository
import com.example.tabletop.repository.UserRepository
import com.example.tabletop.viewmodel.ViewModelFactory
import com.example.tabletop.viewmodel.MockViewModel
import com.example.tabletop.viewmodel.UserViewModel
import com.example.tabletop.util.Constants.LogType
import java.util.*
import kotlin.concurrent.schedule

object Helpers {
    fun AppCompatActivity.justStartActivity(activity: AppCompatActivity) {
        startActivity(Intent(this, activity::class.java))
    }

    fun AppCompatActivity.viewModelOf(repository: Repository): ViewModel {
        val viewModel = when (repository) {
            is UserRepository -> UserViewModel(repository)
            is MockRepository -> MockViewModel(repository)
            else -> throw Exception("Invalid repository")
        }
        return ViewModelProvider(this, ViewModelFactory(repository))
            .get(viewModel::class.java)
    }

    // LOGGING
    fun AppCompatActivity.logIt(
        msg: Any?,
        logType: LogType = LogType.INFO,
        tag: String = this.getClassName()
    ) {
        val logTypeString = "[${logType}] "
        Timer().schedule(1) {
            Log.d("[$tag]", "$logTypeString${msg.toString()}")
        }
    }

    fun AppCompatActivity.logIt(
        vararg msgs: Any?,
        logType: LogType = LogType.INFO,
        tag: String = this.getClassName()
    ) {
        val logTypeString = "[${logType}] "
        msgs.forEach { msg ->
            Timer().schedule(1) {
                Log.d("[$tag]", "$logTypeString${msg.toString()}")
            }
        }
    }

    fun AppCompatActivity.logError(msg: Any?, tag: String = this.getClassName()
    ) {
        val logTypeString = "[ERROR] "
        Timer().schedule(1) {
            Log.d("[$tag]", "$logTypeString${msg.toString()}")
        }
    }

    fun AppCompatActivity.logError(vararg msgs: Any?, tag: String = this.getClassName()) {
        val logTypeString = "[ERROR] "
        msgs.forEach { msg ->
            Timer().schedule(1) {
                Log.d("[$tag]", "$logTypeString${msg.toString()}")
            }
        }
    }

    fun Any.getClassName(): String = this::class.simpleName as String

    fun AppCompatActivity.showToast(msg: String, long: Boolean = false) {
        Toast.makeText(this,
            msg,
            if (long) Toast.LENGTH_LONG else Toast.LENGTH_SHORT).show()
    }

    class MyValidator() {
        fun validate(editText: EditText) {

        }

        private fun isValid(editText: EditText) {

        }
    }
}
