package com.example.tabletop.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tabletop.databinding.ActivityLoginBinding
import com.example.tabletop.repository.UserRepository
import com.example.tabletop.util.Helpers.getEditTextString
import com.example.tabletop.util.Helpers.logIt
import com.example.tabletop.util.Helpers.viewModelOf
import com.example.tabletop.util.LoginRequest
import com.example.tabletop.viewmodel.UserViewModel
import net.alexandroid.utils.mylogkt.logD
import splitties.toast.UnreliableToastApi
import splitties.toast.toast

@UnreliableToastApi
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setup()

        binding.btnLogin.setOnClickListener {
            val (nickname, password) = getEditTextString(
                binding.loginEtNickname,
                binding.loginEtPassword
            )
            if (isFormValid(nickname, password)) {
                logD("All fields are valid")
                // val loginRequest = LoginRequest(nickname, password)
                // loginUser(loginRequest)
            } else {
                toast("Please correct invalid fields")
            }
        }
    }
    private fun setup() {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userViewModel = viewModelOf(UserRepository) as UserViewModel
    }

    private fun isFormValid(nickname: String, password: String): Boolean {

        var areFieldsValid = true
        if (nickname.isEmpty()) {
            binding.loginEtNickname.error = "Field cannot be empty"
            areFieldsValid = false
        }
        if (password.isEmpty()) {
            binding.loginEtPassword.error = "Field cannot be empty"
            areFieldsValid = false
        }
        return areFieldsValid
    }

    private fun loginUser(loginRequest: LoginRequest) {
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
                toast(response.code())
            }
        })
    }
}