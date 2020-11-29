package com.example.tabletop.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tabletop.databinding.ActivityUserLoggedOutBinding
import com.example.tabletop.util.Helpers.justStartActivity

class UserLoggedOutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserLoggedOutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setup()

        //todo splash screen
        /*
        todo later:
         on running app {
             if (user was logged in)
                 start UserNotLoggedInActivity
             else
                 start LoginActivity
         }
        */

        binding.btnGoToLoginActivity.setOnClickListener {
            justStartActivity<LoginActivity>()
        }

        binding.btnGoToRegisterActivity.setOnClickListener {
            justStartActivity<RegisterActivity>()
        }

        binding.btnGoToMainActivity.setOnClickListener {
            justStartActivity<MainActivity>()
        }
    }
    private fun setup() {
        binding = ActivityUserLoggedOutBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}