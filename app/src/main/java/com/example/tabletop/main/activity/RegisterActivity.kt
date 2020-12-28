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
        binding.registerEtEmail.value = "test1@test.test"
        binding.registerEtUsername.value = "test1"
        binding.registerEtFirstname.value = "firstname"
        binding.registerEtLastname.value = "lastname"
        binding.registerEtPassword.value = "qwqwqwqW1$"
        binding.registerEtConfirmPassword.value = binding.registerEtPassword.value
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setup()

        fillForm()

        binding.btnRegister.setOnClickListener {
            val email = binding.registerEtEmail.value
            val username = binding.registerEtUsername.value
            val password =  binding.registerEtPassword.value
            val confirmPassword = binding.registerEtConfirmPassword.value

            val form = RegisterForm(email, username, password, confirmPassword)

            logI("Button clicked with: $form")

            if (isFormValid(form)) {
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


    // ------------------------- REGISTER -----------------------------

    private fun registerUser(user: RegisterRequest) {
        var isAlreadyHandled = false
        UserViewModel.run {
            register(user)
            responseRegister.observe(this@RegisterActivity) {
                if (!(isAlreadyHandled)) {
                    isAlreadyHandled = true
                    handleResponseRegister(it)
                }
            }
        }
    }

    private fun handleResponseRegister(response: Response<RegisterResponse>) {

        val onSuccess = {
            logD(response.status())
            loginUser(
                LoginForm(
                    binding.registerEtFirstname.value,
                    binding.registerEtLastname.value
                )
            )
        }

        val onFailure = {
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

        response.resolve(onSuccess, onFailure)
    }

    // ------------------------- LOGIN -----------------------------

    private fun loginUser(loginForm: LoginForm) {
        var isAlreadyHandled = false
        UserViewModel.run {
            login(loginForm)
            responseLogin.observe(this@RegisterActivity) {
                if (!(isAlreadyHandled)) {
                    isAlreadyHandled = true
                    handleResponseLogin(it)
                }
            }
        }
    }

    private fun handleResponseLogin(response: Response<LoginResponse>) {

        val onSuccess = {
            lifecycleScope.launch {
                response.body()?.let {
                    settingsManager.run {
                        setIsUserLoggedIn(true)
                        setIsFirstRun(false)
                        setUserAccessToken((it.access))
                        setUserRefreshToken(it.refresh)
                    }
                }
            }
            start<MainActivity>()
            finish()
            //editProfile()
        }

        val onFailure = {
            toast("Something went wrong")
        }

        response.resolve(onSuccess, onFailure)
    }

    // ------------------------- PROFILE -----------------------------

    // private fun editProfile() {
    //     lifecycleScope.launch {
    //         settingsManager
    //             .userAccessTokenFlow
    //             .asLiveData()
    //             .observe(this@RegisterActivity) { getUserProfile(it) }
    //     }
    // }
    //
    // private fun getUserProfile(accessToken: String) {
    //     var isAlreadyHandled = false
    //     UserViewModel.run {
    //         getProfile(accessToken)
    //         responseGetProfile.observe(this@RegisterActivity) {
    //             if (!(isAlreadyHandled)) {
    //                 isAlreadyHandled = true
    //                 handleResponseProfile(it, accessToken)
    //             }
    //         }
    //     }
    // }
    //
    // private fun handleResponseProfile(response: Response<Profile>, accessToken: String) {
    //     response.let {
    //         if (it.isSuccessful) {
    //             handleSuccessProfile(it, accessToken)
    //         } else {
    //             handleErrorProfile(it)
    //         }
    //     }
    // }
    //
    // private fun handleSuccessProfile(response: Response<Profile>, accessToken: String) {
    //     val firstname = getEditTextValue(binding.registerEtFirstname)[0]
    //     val lastname = getEditTextValue(binding.registerEtLastname)[0]
    //     val id = response.body()!!.id
    //
    //     val profile = Profile(firstname, lastname, null, id)
    //     logI(profile.toString())
    //
    //     var isAlreadyHandled = false
    //     UserViewModel.run {
    //         editProfile(accessToken, profile)
    //         responseCreateProfile.observe(this@RegisterActivity) {
    //             if (!(isAlreadyHandled)) {
    //                 isAlreadyHandled = true
    //                 handleResponseCreateProfile(it, accessToken)
    //             }
    //         }
    //     }
    // }
    //
    // private fun handleErrorProfile(response: Response<Profile>) {
    //     logE("Pobieranie profilu nie działa, najpewniej nie jestes zalogowany")
    // }
    //
    //
    // // ------------------------- CREATE PROFILE -----------------------------
    //
    // private fun handleResponseCreateProfile(response: Response<Profile>, accessToken: String) {
    //     response.let {
    //         if (it.isSuccessful) {
    //             handleSuccessCreateProfile(it, accessToken)
    //         } else {
    //             handleErrorCreateProfile(it)
    //         }
    //     }
    // }
    //
    // private fun handleSuccessCreateProfile(response: Response<Profile>, accessToken: String) {
    //     logI("Profile created")
    //     start<MainActivity>()
    //     finish()
    // }
    //
    //
    // private fun handleErrorCreateProfile(response: Response<Profile>) {
    //     logI("Failed to create profile")
    // }
}










