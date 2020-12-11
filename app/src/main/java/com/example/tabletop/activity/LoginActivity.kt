package com.example.tabletop.activity

import android.os.Bundle
import android.viewbinding.library.activity.viewBinding
import com.example.tabletop.databinding.ActivityLoginBinding
import com.example.tabletop.repository.UserRepository
import com.example.tabletop.util.Helpers.getEditTextString
import com.example.tabletop.util.Helpers.logIt
import com.example.tabletop.util.LoginRequest
import com.example.tabletop.viewmodel.UserViewModel
import dev.ajkueterman.lazyviewmodels.lazyViewModels
import net.alexandroid.utils.mylogkt.logD
import splitties.toast.UnreliableToastApi
import splitties.toast.toast

@UnreliableToastApi
class LoginActivity : BaseActivity() {

    override val binding: ActivityLoginBinding by viewBinding()

    private val userViewModel: UserViewModel by lazyViewModels { UserViewModel(UserRepository) }

    override fun setup() {

    }

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
        userViewModel.responseOne.observe(this, { response ->
            if (response.isSuccessful) {
                response.run {
                    logIt(
                        body(),
                        code(),
                        message()
                    )
                }
                //start<MainActivity>()
            } else {
                toast(response.code())
            }
        })
    }
}