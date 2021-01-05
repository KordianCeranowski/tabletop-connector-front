package com.example.tabletop.main.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.viewbinding.library.activity.viewBinding
import androidx.lifecycle.observe
import com.example.tabletop.R
import com.example.tabletop.databinding.ActivityRegisterBinding
import com.example.tabletop.mvvm.model.User
import com.example.tabletop.mvvm.model.helpers.*
import com.example.tabletop.mvvm.model.helpers.request.LoginRequest
import com.example.tabletop.mvvm.model.helpers.request.ProfileSimple
import com.example.tabletop.mvvm.model.helpers.request.RegisterRequest
import com.example.tabletop.settings.SettingsManager
import com.example.tabletop.mvvm.viewmodel.UserViewModel
import com.example.tabletop.util.*
import com.livinglifetechway.k4kotlin.core.value
import dev.ajkueterman.lazyviewmodels.lazyViewModels
import kotlinx.coroutines.runBlocking
import net.alexandroid.utils.mylogkt.*
import net.alexandroid.utils.mylogkt.logD
import retrofit2.Response
import splitties.activities.start
import splitties.toast.UnreliableToastApi
import splitties.toast.toast

@Suppress("COMPATIBILITY_WARNING", "SpellCheckingInspection")
@UnreliableToastApi
class RegisterActivity : BaseActivity() {

    override val binding: ActivityRegisterBinding by viewBinding()

    private val userViewModel by lazyViewModels { UserViewModel() }

    private lateinit var settingsManager: SettingsManager

    override fun setup() {
        settingsManager = SettingsManager(applicationContext)
        supportActionBar?.title = "Sign Up"
    }

    // DEVELOPMENT ONLY
    private fun fillForm() {
        binding.registerEtUsername.value = USER_TEST_LOGIN
        binding.registerEtFirstname.value = "Łukasz"
        binding.registerEtLastname.value = "Stanisławowski"
        binding.registerEtPassword.value = USER_TEST_PASSWORD
        binding.registerEtConfirmPassword.value = binding.registerEtPassword.value
    }

    // Top Right Menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.register_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mi_register_goto_login ->  {
                start<LoginActivity>()
                finish()
            }
        }
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setup()

        fillForm()

        attachObservers()

        binding.btnRegister.setOnClickListener {
            val username = binding.registerEtUsername.value
            val password = binding.registerEtPassword.value
            val confirmPassword = binding.registerEtConfirmPassword.value

            val firstname = binding.registerEtFirstname.value
            val lastname = binding.registerEtLastname.value

            val profile = ProfileSimple(firstname, lastname)
            val form = RegisterForm(username, password, confirmPassword, profile)

            if (isFormValid(form)) {
                registerUser(RegisterRequest(username, password, profile))
            } else {
                toast("Please correct invalid fields")
            }
        }
    }

    private fun isFormValid(registerForm: RegisterForm): Boolean {
        var areFieldsValid = true

        if (!(isFieldValid(ValidationPattern.NICKNAME))) {
            areFieldsValid = false
        }
        if (!(isFieldValid(ValidationPattern.PASSWORD))) {
            areFieldsValid = false
        }

        binding.registerEtConfirmPassword.run {
            if (registerForm.password.isEmpty() || value != registerForm.password) {
                areFieldsValid = false.also { error = "Passwords do not match" }
            } else {
                disableError()
            }
        }

        binding.registerEtFirstname.run {
            if (value.isEmpty()) {
                areFieldsValid = false.also { setErrorEmpty() }
            } else {
                disableError()
            }
        }

        binding.registerEtLastname.run {
            if (value.isEmpty()) {
                areFieldsValid = false.also { setErrorEmpty() }
            } else {
                disableError()
            }
        }

        return areFieldsValid
    }

    private fun isFieldValid(myPattern: ValidationPattern): Boolean {
        val (editText, fieldName, pattern) = when (myPattern) {
            ValidationPattern.NICKNAME ->
                Triple(binding.registerEtUsername, "username", ValidationPattern.NICKNAME())
            ValidationPattern.PASSWORD ->
                Triple(binding.registerEtPassword, "password", ValidationPattern.PASSWORD())
        }

        return editText.run {
            if (value.isEmpty()) {
                false.also { setErrorEmpty() }
            } else if (!(pattern.matcher(value).matches())) {
                false.also { setErrorInvalid(fieldName) }
            } else {
                true.also { disableError() }
            }
        }
    }

    private fun attachObservers() {
        userViewModel.run {
            responseOne.observe(this@RegisterActivity) { handleResponseRegister(it) }
            responseLogin.observe(this@RegisterActivity) { handleResponseLogin(it) }
        }
    }

    private fun registerUser(registerRequest: RegisterRequest) {
        userViewModel.register(registerRequest)
    }

    private fun handleResponseRegister(response: Response<User>) {
        val onSuccess = {
            logD(response.status())
            loginUser(
                LoginRequest(binding.registerEtUsername.value, binding.registerEtPassword.value)
            )
        }

        val onFailure = {
            val errorJson = response.getErrorJson()

            logW(response.getFullResponse())
            logW(errorJson.toString())

            val errors = mapOf(
                "username" to "[A user with that username already exists.]"
            )

            handleRegisterErrors(errorJson, errors)
        }

        response.resolve(onSuccess, onFailure)
    }

    private fun handleRegisterErrors(errorJson: Map<String, String>, errors: Map<String, String>) {
        errorJson.forEach { (key, value) ->
            when (key) {
                "username" -> {
                    binding.registerEtUsername.run {
                        if (value == errors["username"]) {
                            error = "Username is already taken".also {
                                toast("Please correct invalid fields")
                            }
                        } else {
                            disableError().also { toast(ERROR_MESSAGE_FAILURE) }
                        }
                    }
                }
            }
        }
    }

    private fun loginUser(loginRequest: LoginRequest) {
        logD(loginRequest.toString())
        userViewModel.login(loginRequest)
    }

    private fun handleResponseLogin(response: Response<LoginResponse>) {
        val onSuccess = {
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

            logI(response.getFullResponse())
            toast(errorJson.toString())
        }

        response.resolve(onSuccess, onFailure)
    }
}
