package com.example.tabletop.main.activity

import android.os.Bundle
import android.viewbinding.library.activity.viewBinding
import com.example.tabletop.databinding.ActivityUserChangePasswordBinding
import com.example.tabletop.mvvm.viewmodel.UserViewModel
import com.example.tabletop.settings.SettingsManager
import com.example.tabletop.util.*
import com.google.gson.JsonObject
import com.livinglifetechway.k4kotlin.core.value
import dev.ajkueterman.lazyviewmodels.lazyViewModels
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import net.alexandroid.utils.mylogkt.logD
import net.alexandroid.utils.mylogkt.logW
import retrofit2.Response
import splitties.toast.toast

@Suppress("EXPERIMENTAL_API_USAGE")
class UserChangePasswordActivity : BaseActivity(), IErrorBodyProperties {

    override val binding: ActivityUserChangePasswordBinding by viewBinding()

    override lateinit var errorBodyProperties: Map<String, String>

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

            if (isFormValid()) {
                val json = JsonObject().apply {
                    addProperty("current_password", currentPassword)
                    addProperty("new_password", newPassword)
                }
                changePassword(accessToken, json)
            } else {
                toast("Please correct invalid fields")
            }
        }
    }

    private fun isFormValid(): Boolean = isNewPasswordValid() && isCurrentPasswordValid()

    private fun isCurrentPasswordValid(): Boolean {
        return binding.etChangePasswordCurrentPassword.run {
            if (value.isEmpty()) {
                false.also { setErrorEmpty() }
            } else {
                true.also { disableError() }
            }
        }
    }

    private fun isNewPasswordValid(): Boolean {
        val pattern = ValidationPattern.PASSWORD()

        return binding.etChangePasswordNewPassword.run {
            if (value.isEmpty()) {
                false.also { setErrorEmpty() }
            } else if (!(pattern.matcher(value).matches())) {
                false.also { setErrorInvalid("password") }
            } else {
                true.also { disableError() }
            }
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

            toast("Password changed")
            finish()
        }

        val onFailure = {
            logW(response.getFullResponse())
            logW(response.getErrorBodyProperties().toString())
            toast(ERROR_MESSAGE_FAILURE)

            if (!(this::errorBodyProperties.isInitialized)) {
                errorBodyProperties = response.getErrorBodyProperties()
            }
            logD(errorBodyProperties.toString())

            val errors = mapOf(
                "current_password" to "[Invalid password.]"
            )

            errorBodyProperties.forEach { (key, value) ->
                when(key) {
                    "current_password" -> {
                        val tempKey = "current_password"
                        if (value == errors[tempKey]) {
                            toast("Invalid password")
                            binding.etChangePasswordCurrentPassword.error = "Invalid password"
                        }
                    }
                }
            }
        }

        response.resolve(onSuccess, onFailure)
    }
}