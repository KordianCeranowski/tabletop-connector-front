package com.example.tabletop.main.activity

import android.os.Bundle
import android.viewbinding.library.activity.viewBinding
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import com.example.tabletop.databinding.ActivityLoginBinding
import com.example.tabletop.mvvm.model.helpers.LoginForm
import com.example.tabletop.mvvm.model.helpers.LoginResponse
import com.example.tabletop.mvvm.viewmodel.UserViewModel
import com.example.tabletop.settings.SettingsManager
import com.example.tabletop.util.*
import com.livinglifetechway.k4kotlin.core.value
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.alexandroid.utils.mylogkt.logD
import net.alexandroid.utils.mylogkt.logI
import net.alexandroid.utils.mylogkt.logW
import retrofit2.Response
import splitties.activities.start
import splitties.toast.UnreliableToastApi
import splitties.toast.toast

@Suppress("COMPATIBILITY_WARNING")
@UnreliableToastApi
class LoginActivity : BaseActivity(), IErrorBodyProperties {

    override val binding: ActivityLoginBinding by viewBinding()

    override lateinit var errorBodyProperties: Map<String, String>

    private lateinit var settingsManager: SettingsManager

    override fun setup() {
        binding
        settingsManager = SettingsManager(applicationContext)
        supportActionBar?.title = "Login"
    }

    // DEVELOPMENT ONLY
    private fun fillForm(isError: Boolean = false) {
        val (username, password) = if (isError) "error" to "error" else "test1" to "qwqwqwqW1$"
        binding.loginEtUsername.value = username
        binding.loginEtPassword.value = password
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setup()

        fillForm()

        binding.btnLogin.setOnClickListener {
            val username = binding.loginEtUsername.value
            val password = binding.loginEtPassword.value

            val loginForm = LoginForm(username, password)

            if (isFormValid(loginForm)) {
                //logD("All fields are valid")
                loginUser(loginForm)
            } else {
                toast("Please correct invalid fields")
            }
        }

        binding.btnGotoRegister.setOnClickListener {
            start<RegisterActivity>()
            finish()
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
        var isAlreadyHandled = false
        UserViewModel.run {
            login(loginForm)
            responseLogin.observe(this@LoginActivity) {
                if (!(isAlreadyHandled)) {
                    isAlreadyHandled = true
                    handleResponse(it) }
                }
        }
    }

    private fun handleResponse(response: Response<LoginResponse>) {

        val onSuccess = {
            logD(response.status())
            lifecycleScope.launch {
                response.body()?.let {
                    withContext(Dispatchers.Default) {
                        settingsManager.run {
                            setIsUserLoggedIn(true)
                            setUserAccessToken(it.access)
                            setUserRefreshToken(it.refresh)
                        }
                    }
                }
                start<MainActivity>()
                finish()
            }
        }

        val onFailure = {
            if (!(this@LoginActivity::errorBodyProperties.isInitialized)) {
                errorBodyProperties = response.getErrorBodyProperties()
            }

            logW(response.getFullResponse())
            logI(errorBodyProperties.toString())

            val key = "detail"
            val value = "No active account found with the given credentials"

            if (errorBodyProperties[key] == value) {
                toast("Invalid credentials")
            } else {
                toast("Something went wrong")
            }
        }

        response.resolve(onSuccess, onFailure)
    }
}
