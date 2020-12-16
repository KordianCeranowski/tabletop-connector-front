package com.example.tabletop.main.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import com.example.tabletop.settings.SettingsManager
import kotlinx.coroutines.launch
import splitties.toast.UnreliableToastApi

@UnreliableToastApi
class StarterActivity : AppCompatActivity() {

    private lateinit var settingsManager: SettingsManager

    fun setup() {
        settingsManager = SettingsManager(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setup()

        //resetSettings()

        lifecycleScope.launch {
            settingsManager.let {
                it.isUserLoggedInFlow
                    .asLiveData()
                    .observe(this@StarterActivity) { isUserLoggedIn ->
                        it.isFirstRunFlow
                            .asLiveData()
                            .observe(this@StarterActivity) { isFirstRun ->
                                startProperActivity(isUserLoggedIn, isFirstRun)
                                finish()
                            }
                    }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        setVisible(true)
    }

    // DEVELOPMENT ONLY
    private fun resetSettings() {
        lifecycleScope.launch {
            settingsManager.setIsUserLoggedIn(false)
            settingsManager.setIsFirstRun(true)
        }
    }

    private fun startProperActivity(isUserLoggedIn: Boolean, isFirstRun: Boolean) {
        val javaClass = when {
            isFirstRun -> RegisterActivity::class
            isUserLoggedIn -> MainActivity::class
            else -> LoginActivity::class
        }.java

        val intent = Intent(this, javaClass)
        startActivity(intent)
    }
}