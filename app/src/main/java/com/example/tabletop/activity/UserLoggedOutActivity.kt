package com.example.tabletop.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tabletop.databinding.ActivityUserLoggedOutBinding
import com.example.tabletop.util.runLoggingConfig
import splitties.activities.start
import splitties.toast.UnreliableToastApi

@UnreliableToastApi
class UserLoggedOutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserLoggedOutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setup()

        binding.btnGoToMainActivity.setOnClickListener {
            start<MainActivity>()
        }

        binding.btnGoToTestActivity.setOnClickListener {
            start<TestActivity>()
        }

        binding.btnGoToLoginActivity.setOnClickListener {
            start<LoginActivity>()
        }

        binding.btnGoToRegisterActivity.setOnClickListener {
            start<RegisterActivity>()
        }
    }

    private fun setup() {
        runLoggingConfig()

        binding = ActivityUserLoggedOutBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}