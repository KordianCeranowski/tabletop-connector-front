package com.example.tabletop.main.activity

import android.os.Bundle
import android.viewbinding.library.activity.viewBinding
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import com.example.tabletop.databinding.ActivityRegisterBinding
import com.example.tabletop.mvvm.model.helpers.*
import com.example.tabletop.settings.SettingsManager
import com.example.tabletop.mvvm.viewmodel.UserViewModel
import com.example.tabletop.util.*
import com.livinglifetechway.k4kotlin.core.value
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

    override lateinit var errorBodyProperties: Map<String, String>

    private lateinit var settingsManager: SettingsManager

    override fun setup() {
        settingsManager = SettingsManager(applicationContext)
        supportActionBar?.title = "Register"
    }

    // DEVELOPMENT ONLY
    private fun fillForm() {
        binding.registerEtEmail.value = "test1@test.test"
        binding.registerEtUsername.value = "test1"
        binding.registerEtPassword.value = "qwqwqwqW1$"
        binding.registerEtConfirmPassword.value = binding.registerEtPassword.value
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setup()

        fillForm()

        binding.btnRegister.setOnClickListener {
            val email = getEditTextValue(binding.registerEtEmail)[0]
            val firstname = getEditTextValue(binding.registerEtFirstname)[0]
            val lastname = getEditTextValue(binding.registerEtLastname)[0]
            val username = getEditTextValue(binding.registerEtUsername)[0]
            val password = getEditTextValue(binding.registerEtPassword)[0]
            val confirmPassword = getEditTextValue(binding.registerEtConfirmPassword)[0]
            val form = RegisterForm(email, username, password, confirmPassword)

            logI("Button clicked with: $form")

            if (isFormValid(form)) {
                logD("Form is valid")
                registerUser(RegisterRequest(email, username, password))

                lifecycleScope.launch {
                    settingsManager
                        .userAccessTokenFlow
                        .asLiveData()
                        .observe(this@RegisterActivity) { authToken ->
                            UserViewModel.run {
                                getProfile(authToken)
                                responseGetProfile.observe(this@RegisterActivity) {
                                    if (it.isSuccessful) {
                                        logI("Elo")
                                        val profile = Profile(firstname, lastname, "not_set", it.body()!!.id)
                                        logI(profile.toString())
                                        editProfile(authToken, profile)
                                        responseCreateProfile.observe(this@RegisterActivity) {
                                            logI(it.getFullResponse())
                                        }
                                    } else {
                                        logE("Pobieranie profilu nie działa, najpewniej nie jestes zalogowany")
                                    }
                                }
                            }
                        }
                }

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
        UserViewModel.run {
            register(user)
            var isAlreadyHandled = false
            responseRegister.observe(this@RegisterActivity) {
                if (!(isAlreadyHandled)) {
                    isAlreadyHandled = true
                    handleResponse(it, LoginForm(user.username, user.password))
                }
            }
        }
    }

    private fun handleResponse(response: Response<RegisterResponse>, loginForm: LoginForm) {
        if (response.isSuccessful) {
            handleSuccess(response, loginForm)
        } else {
            handleError(response)
        }
    }

    private fun handleSuccess(response: Response<RegisterResponse>, loginForm: LoginForm) {
        logD(response.status())
        lifecycleScope.launch { loginUser(loginForm) }
    }

    private fun handleError(response: Response<RegisterResponse>) {
        if (!(this@RegisterActivity::errorBodyProperties.isInitialized)) {
            errorBodyProperties = response.getErrorBodyProperties()
        }

        logW(response.getFullResponse())
        toast("Please correct invalid fields")

        logI(errorBodyProperties.toString())

        val key1 = "username"
        val value1 = "[A user with that username already exists.]"

        if (errorBodyProperties[key1] == value1) {
            binding.registerEtUsername.error = "Username is already taken"
        }
        // if (errorBodyProperties[key] == value) {
        //     binding.registerEtEmail.error = "Email is already taken"
        // }
    }

    private fun loginUser(loginForm: LoginForm) {
        UserViewModel.run {
            login(loginForm)
            responseLogin.observe(this@RegisterActivity) { handleResponse(it) }
        }
    }

    private fun handleResponse(response: Response<LoginResponse>) {
        if (response.isSuccessful) {
            handleSuccess(response)
        } else {
            handleError(response)
        }
    }

    private fun handleSuccess(response: Response<LoginResponse>) {
        lifecycleScope.launch {
            settingsManager.run {
                response.body()?.let {
                    setIsUserLoggedIn(true)
                    setIsFirstRun(false)
                    setUserAccessToken((it.access))
                    setUserRefreshToken(it.refresh)
                }
            }
            start<MainActivity>()
            finish()
        }
    }

    @JvmName("handleErrorLoginResponse")
    private fun handleError(response: Response<LoginResponse>) {
        toast("Something went wrong")
    }
}










