package com.example.tabletop.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tabletop.databinding.ActivityRegisterBinding
import com.example.tabletop.model.RegisterRequest
import com.example.tabletop.repository.UserRepository
import com.example.tabletop.util.Constants
import com.example.tabletop.util.Constants.MyPattern.*
import com.example.tabletop.util.Helpers.getEditTextString
import com.example.tabletop.util.Helpers.logError
import com.example.tabletop.util.Helpers.logIt
import com.example.tabletop.util.Helpers.showToast
import com.example.tabletop.util.Helpers.viewModelOf
import com.example.tabletop.viewmodel.UserViewModel

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private lateinit var userViewModel: UserViewModel

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
            if (isFormValid(email, nickname, password, confirmPassword)) {
                logIt("All fields are valid")
                registerUser(RegisterRequest(email, nickname, password))
            } else {
                showToast("Please correct invalid fields")
            }
        }
    }

    private fun setup() {
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userViewModel = viewModelOf(UserRepository) as UserViewModel
    }

    private fun isFormValid(
        email: String,
        nickname: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        var areFieldsValid = true
        if (!(isFieldValid(email, EMAIL_PATTERN))) {
            areFieldsValid = false
            logError("email: $email")
        }
        if (!(isFieldValid(nickname, NICKNAME_PATTERN))) {
            areFieldsValid = false
            logError("nickname: $nickname")
        }
        if (!(isFieldValid(password, PASSWORD_PATTERN))) {
            areFieldsValid = false
            logError("password: $password")
        }
        if (password.isEmpty() || confirmPassword != password) {
            areFieldsValid = false
            logError("confirmPassword: $confirmPassword")
            binding.registerEtConfirmPassword.error = "Passwords do not match"
        } else {
            binding.registerEtConfirmPassword.error = null
        }
        return areFieldsValid
    }

    private fun isFieldValid(field: String, myPattern: Constants.MyPattern): Boolean {
        val (editText, fieldName, pattern) = when (myPattern) {
            EMAIL_PATTERN ->
                Triple(binding.registerEtEmail, "email", EMAIL_PATTERN.value)
            NICKNAME_PATTERN ->
                Triple(binding.registerEtNickname, "nickname", NICKNAME_PATTERN.value)
            PASSWORD_PATTERN ->
                Triple(binding.registerEtPassword, "password", PASSWORD_PATTERN.value)
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

    private fun registerUser(registerRequest: RegisterRequest) {
        userViewModel.register(registerRequest)
        userViewModel.responseSingle.observe(this, { response ->
            if (response.isSuccessful) {
                response.run {
                    logIt(
                        body(),
                        code(),
                        message()
                    )
                }
            } else {
                showToast(response.code())
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