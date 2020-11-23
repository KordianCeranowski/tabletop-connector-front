package com.example.tabletop.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tabletop.R
import com.example.tabletop.utils.Helpers.justStartActivity
import com.example.tabletop.utils.Helpers.logIt
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnLogin.setOnClickListener {
            val nickname = etLoginNickname.text.toString()
            val password = etLoginPassword.text.toString()
            val stars = password.replace(".".toRegex(), "*")
            logIt("Nickname: $nickname | Password: $stars")




            justStartActivity(MainActivity())
        }
    }
}