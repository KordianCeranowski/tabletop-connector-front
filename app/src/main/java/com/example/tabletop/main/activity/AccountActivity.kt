package com.example.tabletop.main.activity

import android.os.Bundle
import android.viewbinding.library.activity.viewBinding
import com.example.tabletop.databinding.ActivityAccountBinding
import splitties.activities.start

class AccountActivity : BaseActivity() {

    override val binding: ActivityAccountBinding by viewBinding()

    override fun setup() {
        setActionBarTitle("Account")
    }

    private fun setActionBarTitle(title: String) {
        supportActionBar?.title = title
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setup()

        binding.btnGotoUserChangeUsernameActivity.setOnClickListener {
            start<UserChangeUsernameActivity>()
        }

        binding.btnGotoUserChangePasswordActivity.setOnClickListener {
            start<UserChangePasswordActivity>()
        }

        binding.btnDeleteAccount.setOnClickListener {
            //todo show confirmation alert dialog
        }
    }
}