package com.example.tabletop.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns.EMAIL_ADDRESS
import android.widget.Toast
import com.example.tabletop.R
import com.example.tabletop.model.RegisterRequest
import com.example.tabletop.repository.UserRepository
import com.example.tabletop.utils.Helpers.logIt
import com.example.tabletop.utils.Helpers.viewModelOf
import com.example.tabletop.viewModels.UserViewModel
import kotlinx.android.synthetic.main.activity_register.*
import java.util.regex.Pattern
import kotlin.Exception

class RegisterActivity : AppCompatActivity() {

    private val _NICKNAME: Pattern =
        Pattern.compile(
            "^" +
                    "(?=.*[A-z])"+      // at least 1 letter
                    ".{2,}" +           // at least 2 characters
                    "$")

    private val _PASSWORD: Pattern =
        Pattern.compile(
            "^" +
                    "(?=.*[0-9])" +     // at least 1 digit
                    "(?=.*[a-z])"+      // at least 1 lower case letter
                    "(?=.*[A-Z])"+      // at least 1 upper case letter
                    "(?=.*[@#$%^&+=])"+ // at least 1 special character
                    "(?=\\S+$)"+        // now white spaces
                    ".{8,}" +           // at least 8 characters
                    "$")

    private lateinit var userViewModel: UserViewModel

    private fun init() {
        userViewModel = viewModelOf(UserRepository()) as UserViewModel
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

            if (isFieldValid(email, EMAIL_ADDRESS)) {
                areFieldsValid = false
            }
            if (isFieldValid(nickname, _NICKNAME)) {
                areFieldsValid = false
            }
            if (isFieldValid(password, _PASSWORD)) {
                areFieldsValid = false
            }
            if (password.isEmpty() || confirmPassword != password) {
                etRegisterConfirmPassword.error = "Passwords do not match"
            } else {
                etRegisterConfirmPassword.error = null
            }
            if (areFieldsValid) {
                logIt("All fields are valid")
                 // val registerRequest = RegisterRequest(email, nickname, password)
                 // createUser(registerRequest)
            }
        }
    }

    private fun isFieldValid(field: String, pattern: Pattern, ): Boolean {
        val editText = when (pattern) {
            EMAIL_ADDRESS -> etRegisterEmail
            _NICKNAME -> etRegisterNickname
            _PASSWORD -> etRegisterPassword
            else -> throw Exception("Invalid pattern")
        }
        val fieldName = when (pattern) {
            EMAIL_ADDRESS -> "email"
            _NICKNAME -> "nickname"
            else -> "password"
        }
        logIt("Checking [$field]...")

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

    private fun createUser(registerRequest: RegisterRequest) {
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
                Toast.makeText(this, response.code(), Toast.LENGTH_SHORT).show()
                // if (!isEmailTaken()) {
                //     etRegisterEmail.error = "Email is already taken"
                // } else {
                //     etRegisterEmail.error = null
                // }
                //
                // if (!isNicknameTaken()) {
                //     etRegisterNickname.error = "Nickname is already taken"
                // } else {
                //     etRegisterNickname.error = null
                // }
            }
        })
    }
}