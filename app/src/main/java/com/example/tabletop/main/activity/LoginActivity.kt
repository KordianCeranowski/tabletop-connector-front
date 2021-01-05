package com.example.tabletop.main.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.viewbinding.library.activity.viewBinding
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
import kotlinx.coroutines.runBlocking
import net.alexandroid.utils.mylogkt.logD
import net.alexandroid.utils.mylogkt.logI
import net.alexandroid.utils.mylogkt.logW
import retrofit2.Response
import splitties.activities.start
import splitties.toast.UnreliableToastApi
import splitties.toast.toast

@Suppress("COMPATIBILITY_WARNING")
@UnreliableToastApi
class LoginActivity : BaseActivity() {

    override val binding: ActivityLoginBinding by viewBinding()

    private val userViewModel by lazyViewModels { UserViewModel() }

    private lateinit var settingsManager: SettingsManager

    override fun setup() {
        binding
        settingsManager = SettingsManager(applicationContext)
        supportActionBar?.title = "Sign In"
    }

    // DEVELOPMENT ONLY
    private fun fillForm(isError: Boolean = false) {
        val (username, password) =
            if (isError)
                "error" to "error"
            else
                USER_TEST_LOGIN to USER_TEST_PASSWORD

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

            if (isFormValid()) {
                val loginRequest = LoginRequest(username, password)
                loginUser(loginRequest)
            } else {
                toast("Please correct invalid fields")
            }
        }
    }

    private fun isFormValid(): Boolean {
        var areFieldsValid = true
        binding.loginEtUsername.run {
            if (text.isEmpty()) {
                error = "Field cannot be empty"
                areFieldsValid = false
            } else {
                disableError()
            }
        }
        binding.loginEtPassword.run {
            if (text.isEmpty()) {
                error = "Field cannot be empty"
                areFieldsValid = false
            } else {
                disableError()
            }
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
                    setIsFirstRun(false)
                    setUserAccessToken(body.auth_token)
                    setUserFirstName(body.firstname)
                    setUserId(body.user_id)
                }
            }
            start<MainActivity>()
            finish()
        }

        val onFailure = {
            val errorJson = response.getErrorJson()

            logW(response.getFullResponse())
            logI(errorJson.toString())

            val errors = mapOf(
                "non_field_errors" to "[Unable to log in with provided credentials.]"
            )

            handleErrors(errorJson, errors)
        }

        response.resolve(onSuccess, onFailure)
    }

    private fun handleErrors(errorJson: Map<String, String>, errors: Map<String, String>) {
        errorJson.forEach { (key, value) ->
            when (key) {
                "non_field_errors" -> {
                    if (value == errors["non_field_errors"]) {
                        toast("Invalid credentials")
                    }
                }
            }
        }
    }
}
