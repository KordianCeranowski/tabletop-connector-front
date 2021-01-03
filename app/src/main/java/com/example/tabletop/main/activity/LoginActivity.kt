package com.example.tabletop.main.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.viewbinding.library.activity.viewBinding
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import com.example.tabletop.R
import com.example.tabletop.databinding.ActivityLoginBinding
import com.example.tabletop.mvvm.model.helpers.request.LoginRequest
import com.example.tabletop.mvvm.model.helpers.LoginResponse
import com.example.tabletop.mvvm.viewmodel.UserViewModel
import com.example.tabletop.settings.SettingsManager
import com.example.tabletop.util.*
import com.livinglifetechway.k4kotlin.core.value
import dev.ajkueterman.lazyviewmodels.lazyViewModels
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
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

    private val userViewModel by lazyViewModels { UserViewModel() }

    override lateinit var errorBodyProperties: Map<String, String>

    private lateinit var settingsManager: SettingsManager

    override fun setup() {
        binding
        settingsManager = SettingsManager(applicationContext)
        supportActionBar?.title = "Sign In"
    }

    // DEVELOPMENT ONLY
    private fun fillForm(isError: Boolean = false) {
        val (username, password) = if (isError) "error" to "error" else "testo5325" to "qwqwqwqW1$"
        binding.loginEtUsername.value = username
        binding.loginEtPassword.value = password
    }

    // Top Right Menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.login_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mi_login_goto_register ->  {
                start<RegisterActivity>()
                finish()
            }
        }
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setup()

        fillForm()

        attachObserver()

        binding.btnLogin.setOnClickListener {
            val username = binding.loginEtUsername.value
            val password = binding.loginEtPassword.value

            val loginRequest = LoginRequest(username, password)

            if (isFormValid(loginRequest)) {
                loginUser(loginRequest)
            } else {
                toast("Please correct invalid fields")
            }
        }
    }

    private fun isFormValid(loginRequest: LoginRequest): Boolean {
        var areFieldsValid = true
        if (loginRequest.username.isEmpty()) {
            binding.loginEtUsername.error = "Field cannot be empty"
            areFieldsValid = false
        }
        if (loginRequest.password.isEmpty()) {
            binding.loginEtPassword.error = "Field cannot be empty"
            areFieldsValid = false
        }
        return areFieldsValid
    }

    private fun attachObserver() {
        userViewModel.responseLogin.observe(this) { handleResponse(it) }
    }

    private fun loginUser(loginRequest: LoginRequest) {
        userViewModel.login(loginRequest)
    }

    private fun handleResponse(response: Response<LoginResponse>) {
        val onSuccess = {
            logD(response.status())
            val body = response.body()!!
            runBlocking {
                settingsManager.run {
                    setUserAccessToken(body.auth_token)
                    setUserId(body.user_id)
                }
            }
            start<MainActivity>()
            finish()

        }

        val onFailure = {
            if (!(this::errorBodyProperties.isInitialized)) {
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
