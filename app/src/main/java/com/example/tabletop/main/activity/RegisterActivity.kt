package com.example.tabletop.main.activity

import android.os.Bundle
import android.viewbinding.library.activity.viewBinding
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import com.example.tabletop.databinding.ActivityRegisterBinding
import com.example.tabletop.mvvm.model.helpers.LoginForm
import com.example.tabletop.mvvm.repository.UserRepository
import com.example.tabletop.util.Constants.ValidationPattern
import com.example.tabletop.mvvm.model.helpers.RegisterForm
import com.example.tabletop.mvvm.model.helpers.RegisterRequest
import com.example.tabletop.mvvm.model.helpers.RegisterResponse
import com.example.tabletop.settings.SettingsManager
import com.example.tabletop.util.Helpers.getEditTextValue
import com.example.tabletop.mvvm.viewmodel.UserViewModel
import com.example.tabletop.util.Helpers.getErrorBodyProperties
import com.example.tabletop.util.Helpers.getFullResponse
import com.livinglifetechway.k4kotlin.core.value
import dev.ajkueterman.lazyviewmodels.lazyViewModels
import kotlinx.coroutines.launch
import net.alexandroid.utils.mylogkt.*
import net.alexandroid.utils.mylogkt.logD
import retrofit2.Response
import splitties.activities.start
import splitties.toast.UnreliableToastApi
import splitties.toast.toast

@Suppress("COMPATIBILITY_WARNING")
@UnreliableToastApi
class RegisterActivity : BaseActivity(), IErrorBodyProperties {

    override val binding: ActivityRegisterBinding by viewBinding()

    private val userViewModel: UserViewModel by lazyViewModels { UserViewModel(UserRepository) }

    override lateinit var errorBodyProperties: Map<String, String>

    private lateinit var settingsManager: SettingsManager

    override fun setup() {
        settingsManager = SettingsManager(applicationContext)
        supportActionBar?.title = "Register"
    }

    // DEVELOPMENT ONLY
    private fun fillForm() {
        binding.registerEtEmail.value = "test121@test.test"
        binding.registerEtUsername.value = "test121"
        binding.registerEtPassword.value = "qwqwqwqW4$"
        binding.registerEtConfirmPassword.value = binding.registerEtPassword.value
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setup()

        fillForm()

        binding.btnRegister.setOnClickListener {
            val (email, username, password, confirmPassword) = getEditTextValue(
                binding.registerEtEmail,
                binding.registerEtUsername,
                binding.registerEtPassword,
                binding.registerEtConfirmPassword
            )
            if (isFormValid(RegisterForm(email, username, password, confirmPassword))) {
                logD("Form is valid")
                registerUser(RegisterRequest(email, username, password))
            } else {
                toast("Please correct invalid fields")
            }
        }
    }

    private fun isFormValid(registerForm: RegisterForm): Boolean {
        var areFieldsValid = true
        if (!(isFieldValid(registerForm.email, ValidationPattern.EMAIL))) {
            areFieldsValid = false
            logW("email: ${registerForm.email}")
        }
        if (!(isFieldValid(registerForm.username, ValidationPattern.NICKNAME))) {
            areFieldsValid = false
            logW("username: ${registerForm.username}")
        }
        if (!(isFieldValid(registerForm.password, ValidationPattern.PASSWORD))) {
            areFieldsValid = false
            logW("password: ${registerForm.password}")
        }
        if (registerForm.password.isEmpty() || registerForm.confirmPassword != registerForm.password) {
            areFieldsValid = false
            logW("confirmPassword: ${registerForm.confirmPassword}")
            binding.registerEtConfirmPassword.error = "Passwords do not match"
        }
        return areFieldsValid
    }

    private fun isFieldValid(field: String, myPattern: ValidationPattern): Boolean {
        val (editText, fieldName, pattern) = when (myPattern) {
            ValidationPattern.EMAIL ->
                Triple(
                    binding.registerEtEmail,
                    "email",
                    ValidationPattern.EMAIL.value
                )
            ValidationPattern.NICKNAME ->
                Triple(
                    binding.registerEtUsername,
                    "username",
                    ValidationPattern.NICKNAME.value
                )
            ValidationPattern.PASSWORD ->
                Triple(
                    binding.registerEtPassword,
                    "password",
                    ValidationPattern.PASSWORD.value
                )
        }

        return if (field.isEmpty()) {
            editText.error = "Field cannot be empty"
            false
        } else if (!(pattern.matcher(field).matches())) {
            editText.error = "Please enter a valid $fieldName"
            false
        } else {
            editText.error = null
            true
        }
    }

    private fun registerUser(user: RegisterRequest) {
        userViewModel.run {
            register(user)
            responseRegister.observe(this@RegisterActivity, { response ->
                if (response.isSuccessful) {
                    handleSuccessfulResponse(response, LoginForm(user.username, user.password))
                } else {
                    handleErrorResponse(response)
                }
            })
        }
    }

    private fun handleSuccessfulResponse(
        response: Response<RegisterResponse>,
        loginForm: LoginForm
    ) {
        logD(response.getFullResponse())
        lifecycleScope.launch { loginUser(loginForm) }
    }

    private fun loginUser(loginForm: LoginForm) {
        userViewModel.run {
            login(loginForm)
            responseLogin.observe(this@RegisterActivity) { response ->
                if (response.isSuccessful) {
                    lifecycleScope.launch {
                        settingsManager.run {
                            setIsUserLoggedIn(true)
                            setIsFirstRun(false)
                            response.body()?.let {
                                setUsername(loginForm.username)
                                setUserAccessToken((it.access))
                            }
                        }
                    }
                    start<MainActivity>()
                    finish()
                } else {
                    toast("Something went wrong")
                }
            }
        }
    }

    private fun handleErrorResponse(response: Response<RegisterResponse>) {
        if (!(this::errorBodyProperties.isInitialized)) {
            errorBodyProperties = response.getErrorBodyProperties()
        }

        logW(response.getFullResponse())
        toast("Please correct invalid fields")

        logD(errorBodyProperties.toString())

        val key1 = "username"
        val value1 = "[A user with that username already exists.]"

        if (errorBodyProperties[key1] == value1) {
            binding.registerEtUsername.error = "Username is already taken"
        }
        // if (errorBodyProperties[key] == value) {
        //     binding.registerEtEmail.error = "Email is already taken"
        // }
    }
}










