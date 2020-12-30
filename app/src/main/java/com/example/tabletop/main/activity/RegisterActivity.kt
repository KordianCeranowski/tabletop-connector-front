package com.example.tabletop.main.activity

import android.os.Bundle
import android.viewbinding.library.activity.viewBinding
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.alexandroid.utils.mylogkt.*
import net.alexandroid.utils.mylogkt.logD
import retrofit2.Response
import splitties.activities.start
import splitties.toast.UnreliableToastApi
import splitties.toast.toast

@Suppress("COMPATIBILITY_WARNING", "SpellCheckingInspection")
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
        binding.registerEtEmail.value = "test@test.test"
        binding.registerEtUsername.value = "testo350"
        binding.registerEtFirstname.value = "firstname"
        binding.registerEtLastname.value = "lastname"
        binding.registerEtPassword.value = "qwqwqwqW1$"
        binding.registerEtConfirmPassword.value = binding.registerEtPassword.value
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setup()

        fillForm()

        attachObservers()

        binding.btnRegister.setOnClickListener {
            val email = binding.registerEtEmail.value
            val username = binding.registerEtUsername.value
            val password = binding.registerEtPassword.value
            val confirmPassword = binding.registerEtConfirmPassword.value

            val firstname = binding.registerEtFirstname.value
            val lastname = binding.registerEtLastname.value

            val profile = ProfileSimple(firstname, lastname)
            val form = RegisterForm(email, username, password, confirmPassword, profile)

            logD(form.toString())

            if (isFormValid(form)) {
                logD("Form is valid")
                registerUser(RegisterRequest(email, username, password, profile))
            } else {
                toast("Please correct invalid fields")
            }
        }
    }

    private fun isFormValid(registerForm: RegisterForm): Boolean {
        var areFieldsValid = true

        if (!(isFieldValid(registerForm.email, ValidationPattern.EMAIL))) {
            areFieldsValid = false
        }
        if (!(isFieldValid(registerForm.username, ValidationPattern.NICKNAME))) {
            areFieldsValid = false
        }
        if (!(isFieldValid(registerForm.password, ValidationPattern.PASSWORD))) {
            areFieldsValid = false
        }

        if (registerForm.password.isEmpty()
            || registerForm.confirmPassword != registerForm.password) {
            areFieldsValid = false
            logW("confirmPassword: ${registerForm.confirmPassword}")
            binding.registerEtConfirmPassword.error = "Passwords do not match"
        } else {
            binding.registerEtConfirmPassword.disableError()
        }

        if (registerForm.profile.firstname.isEmpty()) {
            areFieldsValid = false
            binding.registerEtFirstname.setErrorEmpty()
        } else {
            binding.registerEtFirstname.disableError()
        }
        if (registerForm.profile.lastname.isEmpty()) {
            areFieldsValid = false
            binding.registerEtLastname.setErrorEmpty()
        } else {
            binding.registerEtLastname.disableError()
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
            editText.setErrorEmpty()
            false
        } else if (!(pattern.matcher(field).matches())) {
            editText.setErrorInvalid(fieldName)
            false
        } else {
            editText.disableError()
            true
        }
    }

    private fun attachObservers() {
        UserViewModel.run {
            responseOne.observe(this@RegisterActivity) { handleResponseRegister(it) }
            responseLogin.observe(this@RegisterActivity) { handleResponseLogin(it) }
        }
    }

    private fun registerUser(registerRequest: RegisterRequest) {
        UserViewModel.register(registerRequest)
    }

    private fun handleResponseRegister(response: Response<User>) {

        val onSuccess = {
            logD(response.status())
            loginUser(
                LoginRequest(
                    binding.registerEtUsername.value,
                    binding.registerEtPassword.value
                )
            )
        }

        val onFailure = {
            if (!(this::errorBodyProperties.isInitialized)) {
                errorBodyProperties = response.getErrorBodyProperties()
            }

            logW(response.getFullResponse())
            logW(errorBodyProperties.toString())
            toast("Something went wrong REGISTER")

            val errors = mapOf(
                "username" to "[A user with that username already exists.]"
            )

            if (errorBodyProperties["username"] == errors["username"]) {
                binding.registerEtUsername.error = "Username is already taken"
            }
            // if (errorBodyProperties[key] == value) {
            //     binding.registerEtEmail.error = "Email is already taken"
            // }
        }

        response.resolve(onSuccess, onFailure)
    }

    private fun loginUser(loginRequest: LoginRequest) {
        logD(loginRequest.toString())
        UserViewModel.login(loginRequest)
    }

    private fun handleResponseLogin(response: Response<LoginResponse>) {

        val onSuccess = {
            lifecycleScope.launch {
                val body = response.body()!!
                withContext(Dispatchers.Default) {
                    settingsManager.run {
                        setIsFirstRun(false)
                        setUserAccessToken(body.auth_token)
                        setUserId(body.user_id)
                    }
                }
                start<MainActivity>()
                finish()
            }
        }

        val onFailure = {
            if (!(this::errorBodyProperties.isInitialized)) {
                errorBodyProperties = response.getErrorBodyProperties()
            }
            toast("Something went wrong LOGIN")
            logI(response.getFullResponse())
            logI("Error body: $errorBodyProperties")
        }

        response.resolve(onSuccess, onFailure)
    }
}
