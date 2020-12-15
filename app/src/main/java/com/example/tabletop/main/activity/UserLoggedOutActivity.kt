package com.example.tabletop.main.activity

import android.os.Bundle
import android.viewbinding.library.activity.viewBinding
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.example.tabletop.main.activity.sample.LocationActivity
import com.example.tabletop.databinding.ActivityUserLoggedOutBinding
import com.example.tabletop.settings.SettingsManager
import kotlinx.coroutines.launch
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

        binding.btnStartLoginActivity.setOnClickListener {
            start<LoginActivity>()
        }

        binding.btnStartRegisterActivity.setOnClickListener {
            start<RegisterActivity>()
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

        setOnClickListeners()
    }
}