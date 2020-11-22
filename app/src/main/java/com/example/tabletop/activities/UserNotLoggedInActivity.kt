package com.example.tabletop.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tabletop.R
import com.example.tabletop.utils.Helpers.Companion.getClassName
import com.example.tabletop.utils.Helpers.Companion.justStartActivity
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
            justStartActivity(LoginActivity::class)
        }
        btnGoToMainActivity.setOnClickListener {
            justStartActivity(MainActivity::class)
        }
    }
}