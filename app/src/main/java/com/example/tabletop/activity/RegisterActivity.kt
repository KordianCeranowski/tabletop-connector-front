package com.example.tabletop.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import com.example.tabletop.R
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
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var userViewModel: UserViewModel

    // data class Form(
    //     val email: String,
    //     val nickname: String,
    //     val password: String,
    //     val confirmPassword: String
    // )


    //etRegisterEmail.text.toString().trim(),
    //             etRegisterNickname.text.toString().trim(),
    //             etRegisterPassword.text.toString().trim()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        setupViewModel()

        btnRegister.setOnClickListener {
            if (isFormValid()) {
                logIt("All fields are valid")
                //val fieldValues = getTextOf(
                //    etRegisterEmail,
                //    etRegisterNickname,
                //    etRegisterPassword
                // )
                // registerUser(RegisterRequest(fieldValues[0], fieldValues[1], fieldValues[2]))
            } else {
                showToast("Please correct invalid fields")
            }
        }
    }

    private fun setupViewModel() {
        userViewModel = viewModelOf(UserRepository) as UserViewModel
    }

    private fun isFormValid(): Boolean {
        val fieldValues = getEditTextString(
            etRegisterEmail,
            etRegisterNickname,
            etRegisterPassword,
            etRegisterConfirmPassword
        )
        val email = fieldValues[0]
        val nickname = fieldValues[1]
        val password = fieldValues[2]
        val confirmPassword = fieldValues[3]

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
        return areFieldsValid
    }

    private fun isFieldValid(field: String, myPattern: Constants.MyPattern): Boolean {
        val (editText, fieldName, pattern) = when (myPattern) {
            EMAIL_PATTERN ->
                Triple(etRegisterEmail, "email", EMAIL_PATTERN.value)
            NICKNAME_PATTERN ->
                Triple(etRegisterNickname, "nickname", NICKNAME_PATTERN.value)
            PASSWORD_PATTERN ->
                Triple(etRegisterPassword, "password", PASSWORD_PATTERN.value)
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