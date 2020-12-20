package com.example.tabletop.main.activity

import android.os.Bundle
import android.viewbinding.library.activity.viewBinding
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import com.example.tabletop.databinding.ActivityLoginBinding
import com.example.tabletop.mvvm.repository.UserRepository
import com.example.tabletop.util.Helpers.getEditTextString
import com.example.tabletop.mvvm.model.helpers.LoginForm
import com.example.tabletop.settings.SettingsManager
import com.example.tabletop.mvvm.viewmodel.UserViewModel
import com.example.tabletop.util.Helpers.getErrorBodyProperties
import com.example.tabletop.util.Helpers.getFullResponse
import com.livinglifetechway.k4kotlin.core.value
import dev.ajkueterman.lazyviewmodels.lazyViewModels
import kotlinx.coroutines.launch
import net.alexandroid.utils.mylogkt.logD
import net.alexandroid.utils.mylogkt.logE
import net.alexandroid.utils.mylogkt.logI
import net.alexandroid.utils.mylogkt.logW
import splitties.activities.start
import splitties.toast.UnreliableToastApi
import splitties.toast.toast

@Suppress("COMPATIBILITY_WARNING")
@UnreliableToastApi
class LoginActivity : ViewModelActivity() {

    override val binding: ActivityLoginBinding by viewBinding()

    private val userViewModel: UserViewModel by lazyViewModels { UserViewModel(UserRepository) }

    override lateinit var errorBodyProperties: Map<String, String>

    private lateinit var settingsManager: SettingsManager

    override fun setup() {
        settingsManager = SettingsManager(applicationContext)
        supportActionBar?.title = "Login"
    }

    // DEVELOPMENT ONLY
    private fun fillForm(isError: Boolean = false) {
        val nickname = if (isError) "xd" else "test13"
        val password = if (isError) "xd" else "qwqwqwqW4\$"

        binding.loginEtNickname.value = nickname
        binding.loginEtPassword.value = password
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setup()

        fillForm(true)

        binding.btnLogin.setOnClickListener {
            val (nickname, password) = getEditTextString(
                binding.loginEtNickname,
                binding.loginEtPassword
            )
            if (isFormValid(nickname, password)) {
                logD("All fields are valid")
                val loginForm = LoginForm(nickname, password)
                loginUser(loginForm)
            } else {
                toast("Please correct invalid fields")
            }
        }
    }

    private fun isFormValid(nickname: String, password: String): Boolean {
        var areFieldsValid = true
        if (nickname.isEmpty()) {
            binding.loginEtNickname.error = "Field cannot be empty"
            areFieldsValid = false
        }
        if (password.isEmpty()) {
            binding.loginEtPassword.error = "Field cannot be empty"
            areFieldsValid = false
        }
        return areFieldsValid
    }

    private fun loginUser(loginForm: LoginForm) {
        userViewModel.login(loginForm)

        userViewModel.responseLogin.observe(this) { response ->
            if (response.isSuccessful) {
                logD(response.getFullResponse())
                lifecycleScope.launch {
                    settingsManager.apply {
                        setIsUserLoggedIn(true)
                        response.body()?.let {
                            //setUserAccessToken(it.access)
                            //setUsername(it.username)
                        }
                    }
                }
                start<MainActivity>()
                finish()
            } else {
                if (!(this::errorBodyProperties.isInitialized)) {
                    errorBodyProperties = response.getErrorBodyProperties()
                }

                logE(response.getFullResponse())
                toast("Please correct invalid fields")

                logD(errorBodyProperties.toString())

                val key = "detail"
                val value = "No active account found with the given credentials"

                if (errorBodyProperties[key] == value) {
                    binding.loginEtNickname.error = "Username is not registered"
                } else {
                    errorBodyProperties[key]
                }
            }
        }
    }
}
