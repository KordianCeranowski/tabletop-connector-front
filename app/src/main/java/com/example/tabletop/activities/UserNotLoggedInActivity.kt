package com.example.tabletop.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.tabletop.R
import kotlinx.android.synthetic.main.activity_user_not_logged_in.*

class UserNotLoggedInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_not_logged_in)

        //todo splash screen
        /*
        todo later:
         on running app {
             if (user was logged in)
                 start UserNotLoggedInActivity
             else
                 start LoginActivity
         }
        */

        btnGoToLoginActivity.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
                .also { Log.d("MainActivity", "Starting activity: SignInActivity") }
        }
    }
}