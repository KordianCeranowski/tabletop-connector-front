package com.example.tabletop.activity

import android.os.Bundle
import android.viewbinding.library.activity.viewBinding
import com.example.tabletop.activity.sample.LocationActivity
import com.example.tabletop.activity.sample.SidebarActivity
import com.example.tabletop.databinding.ActivityUserLoggedOutBinding
import com.example.tabletop.util.Helpers.getMockEvent
import com.example.tabletop.util.Helpers.startWithExtra
import com.example.tabletop.util.runLoggingConfig
import splitties.activities.start
import splitties.toast.UnreliableToastApi

@UnreliableToastApi
class UserLoggedOutActivity : BaseActivity() {

    override val binding: ActivityUserLoggedOutBinding by viewBinding()

    override fun setup() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setup()

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
}