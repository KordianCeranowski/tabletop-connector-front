package com.example.tabletop.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.tabletop.R
import com.example.tabletop.model.LoginRequest
import com.example.tabletop.model.RegisterRequest
import com.example.tabletop.repository.UserRepository
import com.example.tabletop.utils.Helpers.justStartActivity
import com.example.tabletop.utils.Helpers.logIt
import com.example.tabletop.utils.Helpers.viewModelOf
import com.example.tabletop.viewmodels.UserViewModel
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class LoginActivity : AppCompatActivity() {

    private lateinit var userViewModel: UserViewModel

    private fun init() {
        userViewModel = viewModelOf(UserRepository()) as UserViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        init()

        btnLogin.setOnClickListener {
            val nickname = etLoginNickname.text.toString().trim()
            val password = etLoginPassword.text.toString().trim()

            var areFieldsValid = true
            if (nickname.isEmpty()) {
                etLoginNickname.error = "Field cannot be empty"
                areFieldsValid = false
            }
            if (password.isEmpty()) {
                etLoginPassword.error = "Field cannot be empty"
                areFieldsValid = false
            }
            if (areFieldsValid) {
                logIt("All fields are valid")
                val loginRequest = LoginRequest(nickname, password)
                login(loginRequest)
            }

            justStartActivity(MainActivity())
        }
    }

    private fun login(loginRequest: LoginRequest) {
        userViewModel.login(loginRequest)
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