package com.example.tabletop.main.activity

import android.os.Bundle
import android.viewbinding.library.activity.viewBinding
import com.example.tabletop.databinding.ActivityUserChangePasswordBinding
import com.example.tabletop.mvvm.viewmodel.UserViewModel
import com.example.tabletop.settings.SettingsManager
import com.example.tabletop.util.*
import com.google.gson.JsonObject
import dev.ajkueterman.lazyviewmodels.lazyViewModels
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import net.alexandroid.utils.mylogkt.logD
import net.alexandroid.utils.mylogkt.logW
import retrofit2.Response
import splitties.toast.toast

@Suppress("EXPERIMENTAL_API_USAGE")
class UserChangePasswordActivity : BaseActivity() {

    override val binding: ActivityUserChangePasswordBinding by viewBinding()

    private val userViewModel by lazyViewModels { UserViewModel() }

    private lateinit var settingsManager: SettingsManager

    override fun setup() {
        settingsManager = SettingsManager(applicationContext)
        setActionBarTitle("Change Password")
    }

    private fun setActionBarTitle(title: String) {
        supportActionBar?.title = title
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setup()

        attachObserverChangePassword()

        val accessToken = runBlocking { settingsManager.userAccessTokenFlow.first() }

        binding.btnChangePassword.setOnClickListener {
            val (newPassword, currentPassword) =
                binding.run {
                    Pair(
                        etChangePasswordNewPassword.text.toString(),
                        etChangePasswordCurrentPassword.text.toString()
                    )
                }

            val json = JsonObject().apply {
                addProperty("current_password", currentPassword)
                addProperty("new_password", newPassword)
            }

            changePassword(accessToken, json)
        }
    }

    private fun changePassword(accessToken: String, json: JsonObject) {
        userViewModel.changePassword(accessToken, json)
    }

    private fun attachObserverChangePassword() {
        userViewModel.responseChangePassword.observe(this) { handleResponse(it) }
    }

    private fun handleResponse(response: Response<JsonObject>) {
        val onSuccess = {
            logD(response.status())
            logD(response.getFullResponse())
            //toast("Password changed")
            finish()
        }

        val onFailure = {
            logW(response.getFullResponse())
            logW(response.getErrorBodyProperties().toString())
            toast(ERROR_MESSAGE_FAILURE)
        }

        response.resolve(onSuccess, onFailure)
    }
}