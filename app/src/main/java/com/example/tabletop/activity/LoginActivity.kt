package com.example.tabletop.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.tabletop.R
import com.example.tabletop.model.LoginRequest
import com.example.tabletop.repository.UserRepository
import com.example.tabletop.util.Helpers.logIt
import com.example.tabletop.util.Helpers.showToast
import com.example.tabletop.util.Helpers.viewModelOf
import com.example.tabletop.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setupViewModel()

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
                // val loginRequest = LoginRequest(nickname, password)
                // login(loginRequest)
            }

        }
    }

    private fun setupViewModel() {
        userViewModel = viewModelOf(UserRepository) as UserViewModel
    }

    private fun login(loginRequest: LoginRequest) {
        userViewModel.login(loginRequest)
        userViewModel.responseSingle.observe(this, { response ->
            if (response.isSuccessful) {
                response.run {
                    logIt(
                        body(),
                        code(),
                        message()
                    )
                }
                //justStartActivity(MainActivity())
            } else {
                showToast(response.code())
            }
        })
    }
}