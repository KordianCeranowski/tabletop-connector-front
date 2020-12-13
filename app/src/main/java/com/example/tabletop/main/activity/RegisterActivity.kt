package com.example.tabletop.main.activity

import android.os.Bundle
import android.viewbinding.library.activity.viewBinding
import androidx.lifecycle.lifecycleScope
import com.example.tabletop.databinding.ActivityRegisterBinding
import com.example.tabletop.mvvm.model.User
import com.example.tabletop.mvvm.repository.UserRepository
import com.example.tabletop.util.Constants.ValidationPattern
import com.example.tabletop.mvvm.model.helpers.RegisterForm
import com.example.tabletop.settings.SettingsManager
import com.example.tabletop.util.Helpers.getEditTextString
import com.example.tabletop.util.Helpers.getMockUser
import com.example.tabletop.util.Helpers.logIt
import com.example.tabletop.mvvm.viewmodel.UserViewModel
import dev.ajkueterman.lazyviewmodels.lazyViewModels
import kotlinx.coroutines.launch
import net.alexandroid.utils.mylogkt.*
import net.alexandroid.utils.mylogkt.logD
import splitties.activities.start
import splitties.toast.UnreliableToastApi
import splitties.toast.toast

@UnreliableToastApi
class RegisterActivity : BaseActivity() {

    override val binding: ActivityRegisterBinding by viewBinding()

    private val userViewModel: UserViewModel by lazyViewModels { UserViewModel(UserRepository) }

    private lateinit var settingsManager: SettingsManager

    override fun setup() {
        settingsManager = SettingsManager(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setup()

        binding.btnRegister.setOnClickListener {
            val (email, nickname, password, confirmPassword) = getEditTextString(
                binding.registerEtEmail,
                binding.registerEtNickname,
                binding.registerEtPassword,
                binding.registerEtConfirmPassword
            )
            if (isFormValid(RegisterForm(email, nickname, password, confirmPassword))) {
                logD("Form is valid")
                //registerUser(RegisterRequest(email, nickname, password))
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
            logW("nickname: ${registerForm.username}")
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
        // else {
        //     binding.registerEtConfirmPassword.error = null
        // }
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
                    binding.registerEtNickname,
                    "nickname",
                    ValidationPattern.NICKNAME.value
                )
            ValidationPattern.PASSWORD ->
                Triple(
                    binding.registerEtPassword,
                    "password",
                    ValidationPattern.PASSWORD.value
                )
        }
        // logIt("Checking $fieldName...")

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

    private fun registerUser(user: User) {
        val newUser = getMockUser()
        userViewModel.save(newUser)
        userViewModel.responseOne.observe(this, { response ->
            if (response.isSuccessful) {
                response.run {
                    logIt(
                        body(),
                        code(),
                        message()
                    )
                }
                lifecycleScope.launch {
                    settingsManager.setIsUserLoggedIn(true)
                }
                start<MainActivity>()
            } else {
                toast(response.code())
                /*
                if (!isEmailTaken()) {
                    etRegisterEmail.error = "Email is already taken"
                } else {
                    etRegisterEmail.error = null
                }

                if (!isNicknameTaken()) {
                    etRegisterNickname.error = "Nickname is already taken"
                } else {
                    etRegisterNickname.error = null
                }
                 */
            }
        })
    }
}