package com.example.tabletop.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.tabletop.R
import com.example.tabletop.model.RegisterRequest
import com.example.tabletop.repository.UserRepository
import com.example.tabletop.util.Constants.EMAIL_PATTERN
import com.example.tabletop.util.Constants.NICKNAME_PATTERN
import com.example.tabletop.util.Constants.PASSWORD_PATTERN
import com.example.tabletop.util.Helpers.logError
import com.example.tabletop.util.Helpers.logIt
import com.example.tabletop.util.Helpers.showToast
import com.example.tabletop.util.Helpers.viewModelOf
import com.example.tabletop.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_register.*
import java.util.regex.Pattern
import kotlin.Exception

class RegisterActivity : AppCompatActivity() {


    private lateinit var userViewModel: UserViewModel

    private fun init() {
        userViewModel = viewModelOf(UserRepository) as UserViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        init()

        btnRegister.setOnClickListener {
            val email = etRegisterEmail.text.toString().trim()
            val nickname = etRegisterNickname.text.toString().trim()
            val password = etRegisterPassword.text.toString().trim()
            val confirmPassword = etRegisterConfirmPassword.text.toString().trim()

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
                etRegisterConfirmPassword.error = "Passwords do not match"
            } else {
                etRegisterConfirmPassword.error = null
            }
            if (areFieldsValid) {
                logIt("All fields are valid")
                 // val registerRequest = RegisterRequest(email, nickname, password)
                 // registerUser(registerRequest)
            } else {
                showToast("Please correct invalid fields")
            }
        }
    }

    private fun isFieldValid(field: String, pattern: Pattern): Boolean {
        val (editText, fieldName) = when (pattern) {
            EMAIL_PATTERN -> etRegisterEmail to "email"
            NICKNAME_PATTERN -> etRegisterNickname to "nickname"
            PASSWORD_PATTERN -> etRegisterPassword to "password"
            else -> throw Exception("Invalid pattern")
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
        userViewModel.respUser.observe(this, { response ->
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