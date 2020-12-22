package com.example.tabletop.main.activity

import android.os.Bundle
import android.viewbinding.library.activity.viewBinding
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import com.example.tabletop.databinding.ActivityLoginBinding
import com.example.tabletop.mvvm.repository.UserRepository
import com.example.tabletop.util.getEditTextValue
import com.example.tabletop.mvvm.model.helpers.LoginForm
import com.example.tabletop.mvvm.model.helpers.LoginResponse
import com.example.tabletop.settings.SettingsManager
import com.example.tabletop.mvvm.viewmodel.UserViewModel
import com.example.tabletop.util.getErrorBodyProperties
import com.example.tabletop.util.getFullResponse
import com.livinglifetechway.k4kotlin.core.value
import dev.ajkueterman.lazyviewmodels.lazyViewModels
import kotlinx.coroutines.launch
import net.alexandroid.utils.mylogkt.logD
import net.alexandroid.utils.mylogkt.logE
import retrofit2.Response
import splitties.activities.start
import splitties.toast.UnreliableToastApi
import splitties.toast.toast

@Suppress("COMPATIBILITY_WARNING")
@UnreliableToastApi
class LoginActivity : BaseActivity(), IErrorBodyProperties {

    override val binding: ActivityLoginBinding by viewBinding()

    private val userViewModel: UserViewModel by lazyViewModels { UserViewModel(UserRepository) }

    override lateinit var errorBodyProperties: Map<String, String>

    private lateinit var settingsManager: SettingsManager

    override fun setup() {
        binding
        settingsManager = SettingsManager(applicationContext)
        supportActionBar?.title = "Login"
    }

    // DEVELOPMENT ONLY
    private fun fillForm(isError: Boolean = false) {
        val username = if (isError) "test5" else "test1"
        val password = if (isError) "xd" else "qwqwqwqW1$"

        binding.loginEtUsername.value = username
        binding.loginEtPassword.value = password
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setup()

        fillForm()

        // loginUser(LoginForm("xd", "xd"))
        binding.btnLogin.setOnClickListener {
            val (username, password) = getEditTextValue(
                binding.loginEtUsername,
                binding.loginEtPassword
            )
            val loginForm = LoginForm(username, password)
            if (isFormValid(loginForm)) {
                logD("All fields are valid")
                loginUser(loginForm)
            } else {
                toast("Please correct invalid fields")
            }
        }
    }

    private fun isFormValid(loginForm: LoginForm): Boolean {
        var areFieldsValid = true
        if (loginForm.username.isEmpty()) {
            binding.loginEtUsername.error = "Field cannot be empty"
            areFieldsValid = false
        }
        if (loginForm.password.isEmpty()) {
            binding.loginEtPassword.error = "Field cannot be empty"
            areFieldsValid = false
        }
        return areFieldsValid
    }

    private fun loginUser(loginForm: LoginForm) {
        userViewModel.run {
            login(loginForm)
            responseLogin.observe(this@LoginActivity) { it.handleResponse() }
        }
    }

    private fun Response<LoginResponse>.handleResponse() {
        if (isSuccessful) {
            handleSuccess()
        } else {
            handleError()
        }
    }

    private fun Response<LoginResponse>.handleSuccess() {
        logD(getFullResponse())

        lifecycleScope.launch {
            settingsManager.run {
                body()?.let {
                    setIsUserLoggedIn(true)
                    setUserAccessToken(it.access)
                    setUserRefreshToken(it.refresh)
                }
            }
        }
        start<MainActivity>()
        finish()
    }

    private fun Response<LoginResponse>.handleError() {
        if (!(this@LoginActivity::errorBodyProperties.isInitialized)) {
            errorBodyProperties = getErrorBodyProperties()
        }

        logE(getFullResponse())
        logD(errorBodyProperties.toString())

        val key = "detail"
        val value = "No active account found with the given credentials"

        if (errorBodyProperties[key] == value) {
            toast("Invalid credentials")
        } else {
            toast("Something went wrong")
        }
    }
}
