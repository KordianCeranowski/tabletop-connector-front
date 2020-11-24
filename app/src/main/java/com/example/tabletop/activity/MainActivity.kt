package com.example.tabletop.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tabletop.R
import com.example.tabletop.repository.MockRepository
import com.example.tabletop.util.Helpers.logIt
import com.example.tabletop.util.Helpers.viewModelOf
import com.example.tabletop.viewmodel.MockViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var mockViewModel: MockViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mockViewModel = viewModelOf(MockRepository()) as MockViewModel

        mockViewModel.getPost()

        mockViewModel.response.observe(this, { response ->
            if (response.isSuccessful) {
                response.body()?.run {
                    logIt(
                        "User ID: $userId",
                        "Title: $title",
                        "Body: $body"
                    )
                    testTextView.text = title
                }
            } else {
                logIt(response.errorBody())
                testTextView.text = response.code().toString()
            }
        })

        /*
        todo:
          splash screen
          recycler view
          sidebar
        */
    }
}