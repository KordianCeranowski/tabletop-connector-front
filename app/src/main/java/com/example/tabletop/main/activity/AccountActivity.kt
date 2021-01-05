package com.example.tabletop.main.activity

import android.os.Bundle
import android.viewbinding.library.activity.viewBinding
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.example.tabletop.R
import com.example.tabletop.databinding.ActivityAccountBinding
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
import splitties.activities.start

@Suppress("EXPERIMENTAL_API_USAGE")
class AccountActivity : BaseActivity() {

    override val binding: ActivityAccountBinding by viewBinding()

    private val userViewModel by lazyViewModels { UserViewModel() }

    private lateinit var settingsManager: SettingsManager

    private lateinit var etPassword: EditText

    private lateinit var dialog: AlertDialog

    override fun setup() {
        settingsManager = SettingsManager(applicationContext)
        setActionBarTitle("Account")
    }

    private fun setActionBarTitle(title: String) {
        supportActionBar?.title = title
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setup()

        attachObserverDeleteMyAccount()

        binding.btnGotoUserChangeUsernameActivity.setOnClickListener {
            start<UserChangeUsernameActivity>()
        }

        binding.btnGotoUserChangePasswordActivity.setOnClickListener {
            start<UserChangePasswordActivity>()
        }

        val accessToken = runBlocking { settingsManager.userAccessTokenFlow.first() }

        binding.btnDeleteAccount.setOnClickListener {
            showAlertDialogDeleteMyAccount(accessToken)
        }
    }

    private fun showAlertDialogDeleteMyAccount(accessToken: String) {
        val layout = layoutInflater.inflate(R.layout.alert_dialog_delete_my_account, null)

        val builder = AlertDialog.Builder(this).apply {
            setTitle("Confirm")
            setMessage("Are you sure you want to delete your account?")

            setView(layout)

            setPositiveButton("OK", null)
            setNegativeButton("Cancel") { _, _ -> }
        }

        dialog = builder.create()

        dialog.setOnShowListener {
            etPassword = layout.findViewById(R.id.et_delete_my_account_password)

            val okButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)

            okButton.setOnClickListener {
                if (isCurrentPasswordValid(etPassword)) {
                    val json = JsonObject().apply {
                        addProperty("current_password", etPassword.value)
                    }
                    logD("Sending request")
                    deleteMyAccount(accessToken, json)
                }
            }
        }

        dialog.show()
    }

    private fun isCurrentPasswordValid(editText: EditText): Boolean {
        return editText.run {
            if (value.isEmpty()) {
                false.also { setErrorEmpty() }
            } else {
                true.also { disableError() }
            }
        }
    }

    private fun deleteMyAccount(accessToken: String, json: JsonObject) {
        userViewModel.deleteMyAccount(accessToken, json)
    }

    private fun attachObserverDeleteMyAccount() {
        userViewModel.responseDeleteMyAccount.observe(this) { handleResponse(it) }
    }

    private fun handleResponse(response: Response<JsonObject>) {
        val onSuccess = {
            logD(response.status())

            dialog.dismiss()

            runBlocking {
                settingsManager.run {
                    setUserFirstName("")
                    setUserId("")
                    setUserAccessToken("")
                }
            }
            finishAffinity()
            start<LoginActivity>()
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
                    val tempKey = "current_password"
                    if (value == errors[tempKey]) {
                        etPassword.error = "Invalid password"
                    }
                }
            }
        }
    }
}