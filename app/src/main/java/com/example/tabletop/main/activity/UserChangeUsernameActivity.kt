package com.example.tabletop.main.activity

import android.os.Bundle
import android.viewbinding.library.activity.viewBinding
import com.example.tabletop.databinding.ActivityUserChangeUsernameBinding
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
class UserChangeUsernameActivity : BaseActivity() {

    override val binding: ActivityUserChangeUsernameBinding by viewBinding()

    private val userViewModel by lazyViewModels { UserViewModel() }

    private lateinit var settingsManager: SettingsManager

    override fun setup() {
        settingsManager = SettingsManager(applicationContext)
        setActionBarTitle("Change Username")
    }

    private fun setActionBarTitle(title: String) {
        supportActionBar?.title = title
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setup()

        attachObserverChangeUsername()

        val accessToken = runBlocking { settingsManager.userAccessTokenFlow.first() }

        binding.btnChangeUsername.setOnClickListener {
            val (newUsername, currentPassword) =
                binding.run {
                    Pair(
                        etChangeUsernameNewUsername.text.toString(),
                        etChangeUsernameCurrentPassword.text.toString()
                    )
                }
            if (isFormValid()) {
                val json = JsonObject().apply {
                    addProperty("current_password", currentPassword)
                    addProperty("new_username", newUsername)
                }
                changeUsername(accessToken, json)
            } else {
                toast("Please correct invalid fields")
            }
        }
    }

    private fun isFormValid(): Boolean {
        var areFieldsValid = true
        binding.etChangeUsernameNewUsername.run {
            if (value.isEmpty()) {
                error = "Field cannot be empty"
                areFieldsValid = false
            } else {
                disableError()
            }
        }
        binding.etChangeUsernameCurrentPassword.run {
            if (value.isEmpty()) {
                error = "Field cannot be empty"
                areFieldsValid = false
            } else {
                disableError()
            }
        }
        return areFieldsValid
    }


    private fun changeUsername(accessToken: String, json: JsonObject) {
        userViewModel.changeUsername(accessToken, json)
    }

    private fun attachObserverChangeUsername() {
        userViewModel.responseChangeUsername.observe(this) { handleResponse(it) }
    }

    private fun handleResponse(response: Response<JsonObject>) {
        val onSuccess = {
            logD(response.status())

            toast("Username changed")
            finish()
        }

        val onFailure = {
            val errorJson = response.getErrorJson()

            logW(response.getFullResponse())
            logW(errorJson.toString())

            val errors = mapOf(
                "current_password" to "[Invalid password.]"
            )

            handleErrors(errorJson, errors)
        }

        response.resolve(onSuccess, onFailure)
    }
    private fun handleErrors(errorJson: Map<String, String>, errors: Map<String, String>) {
        errorJson.forEach { (key, value) ->
            when (key) {
                "current_password" -> {
                    binding.etChangeUsernameCurrentPassword.run {
                        if (value == errors["current_password"]) {
                            error = "Invalid password".also { toast("Invalid password") }
                        } else {
                            disableError().also { toast(ERROR_MESSAGE_FAILURE) }
                        }
                    }
                }
            }
        }
    }
}