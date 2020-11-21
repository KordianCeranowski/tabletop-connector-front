package com.example.tabletop.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.tabletop.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnLogin.setOnClickListener {
            val nickname = etLoginNickname.text.toString()
            val password = etLoginPassword.text.toString()
            val stars = password.replace(".".toRegex(), "*")
            Log.d("SignInActivity",
                "Nickname: $nickname | Password: $stars")
        }
    }
}