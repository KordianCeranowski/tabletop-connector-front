package com.example.tabletop.main.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tabletop.settings.SettingsManager
import com.example.tabletop.util.runLoggingConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import net.alexandroid.utils.mylogkt.logD
import net.alexandroid.utils.mylogkt.logI
import net.alexandroid.utils.mylogkt.logV
import net.alexandroid.utils.mylogkt.logW
import splitties.toast.UnreliableToastApi

@Suppress("COMPATIBILITY_WARNING")
@UnreliableToastApi
class StarterActivity : AppCompatActivity() {

    private lateinit var settingsManager: SettingsManager

    fun setup() {
        settingsManager = SettingsManager(applicationContext)
        runLoggingConfig()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setup()

        //resetSettings()

        val isFirstRun = runBlocking {
            settingsManager.isFirstRunFlow.first().also { logV("Initial Is First Run: [$it]") }
        }
        val accessToken = runBlocking {
            settingsManager.userAccessTokenFlow.first().also {
                logV("Initial Access Token: [$it]")
            }
        }
        runBlocking {
            settingsManager.userFirstNameFlow.first().also {
                logV("Initial User First Name: [$it]")
            }
        }
        startProperActivity(isFirstRun, accessToken)
    }

    override fun onStart() {
        super.onStart()
        setVisible(true)
    }

    private fun startProperActivity(isFirstRun: Boolean, accessToken: String) {
        logI("Starting proper activity")
        val javaClass = when {
            isFirstRun -> RegisterActivity::class
            accessToken.isNotEmpty() -> MainActivity::class
            else -> LoginActivity::class
        }.java

        val intent = Intent(this, javaClass)
        startActivity(intent)
        finish()
    }

    // DEVELOPMENT ONLY
    private fun resetSettings() {
        runBlocking {
            settingsManager.run {
                setIsFirstRun(true)
                setUserAccessToken("")
                setUserId("")
            }
        }
    }
}