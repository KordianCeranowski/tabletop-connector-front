package com.example.tabletop.activity

import android.os.Bundle
import android.viewbinding.library.activity.viewBinding
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.example.tabletop.activity.sample.LocationActivity
import com.example.tabletop.activity.sample.SidebarActivity
import com.example.tabletop.databinding.ActivityUserLoggedOutBinding
import com.example.tabletop.settings.SettingsManager
import com.example.tabletop.util.Helpers.className
import com.example.tabletop.util.Helpers.getMockEvent
import com.example.tabletop.util.Helpers.startWithExtra
import com.example.tabletop.util.runLoggingConfig
import kotlinx.coroutines.flow.observeOn
import kotlinx.coroutines.launch
import net.alexandroid.utils.mylogkt.logI
import splitties.activities.start
import splitties.toast.UnreliableToastApi

@UnreliableToastApi
class UserLoggedOutActivity : BaseActivity() {

    override val binding: ActivityUserLoggedOutBinding by viewBinding()

    private lateinit var settingsManager: SettingsManager

    override fun setup() {
        settingsManager = SettingsManager(applicationContext)
    }

    private fun setOnClickListeners() {
        binding.btnStartMainActivity.setOnClickListener {
            start<MainActivity>()
        }

        binding.btnStartTestActivity.setOnClickListener {
            start<SidebarActivity>()
        }

        binding.btnStartLoginActivity.setOnClickListener {
            start<LoginActivity>()
        }

        binding.btnStartRegisterActivity.setOnClickListener {
            start<RegisterActivity>()
        }

        binding.btnStartEventActivity.setOnClickListener {
            val event = getMockEvent()
            startWithExtra<EventActivity>("EVENT" to event)
        }

        binding.btnStartEventFormActivity.setOnClickListener {
            start<EventFormActivity>()
        }

        binding.btnStartLocationActivity.setOnClickListener {
            start<LocationActivity>()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setup()

        logI("Created ${this.className}")

        //lifecycleScope.launch { settingsManager.setIsUserLoggedIn(true) }

        settingsManager.isUserLoggedInFlow.asLiveData().observe(this) { isUserLoggedIn ->
            if (isUserLoggedIn) {
                start<MainActivity>()
            }
        }

        setOnClickListeners()
    }
}