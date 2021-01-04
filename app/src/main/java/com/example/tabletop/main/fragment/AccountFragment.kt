package com.example.tabletop.main.fragment

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import com.example.tabletop.R
import com.example.tabletop.databinding.FragmentAccountBinding
import com.example.tabletop.main.activity.UserChangePasswordActivity
import com.example.tabletop.main.activity.UserChangeUsernameActivity
import com.example.tabletop.settings.SettingsManager
import com.example.tabletop.util.className
import net.alexandroid.utils.mylogkt.logI
import splitties.fragments.start
import splitties.views.onClick

class AccountFragment : BaseFragment(R.layout.fragment_account) {

    override val binding: FragmentAccountBinding by viewBinding()

    fun setup() {
        binding
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
