package com.example.tabletop.main.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import com.example.tabletop.mvvm.model.helpers.request.RefreshRequest
import com.example.tabletop.mvvm.model.helpers.RefreshResponse
import com.example.tabletop.mvvm.viewmodel.UserViewModel
import com.example.tabletop.settings.SettingsManager
import com.example.tabletop.util.getErrorBodyProperties
import com.example.tabletop.util.resolve
import com.example.tabletop.util.runLoggingConfig
import com.example.tabletop.util.status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.alexandroid.utils.mylogkt.logD
import net.alexandroid.utils.mylogkt.logI
import net.alexandroid.utils.mylogkt.logV
import net.alexandroid.utils.mylogkt.logW
import retrofit2.Response
import splitties.activities.start
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

        lifecycleScope.launch {
            settingsManager.run {
                val isFirstRun = isFirstRunFlow.first().also {
                    logV("Initial Is First Run: [$it]")
                }
                val accessToken = userAccessTokenFlow.first().also {
                    logV("Initial Access Token: [$it]")
                }

                startProperActivity(isFirstRun, accessToken)
            }
        }
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
    }

    // DEVELOPMENT ONLY
    private fun resetSettings() {
        lifecycleScope.launch {
            withContext(Dispatchers.Default) {
                settingsManager.run {
                    setIsFirstRun(true)
                    setUserAccessToken("")
                    setUserId("")
                }
            }
        }
    }
}