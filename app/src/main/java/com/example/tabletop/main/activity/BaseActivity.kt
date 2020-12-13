package com.example.tabletop.main.activity

import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.example.tabletop.util.runLoggingConfig

abstract class BaseActivity: AppCompatActivity() {

    protected abstract val binding: ViewBinding

    protected abstract fun setup()

    companion object {
        private var isLoggingConfigured = false
        init {
            if (!(isLoggingConfigured)) {
                runLoggingConfig()
                isLoggingConfigured = false
            }
        }
    }
}