package com.example.tabletop.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.tabletop.R
import com.example.tabletop.repository.PostRepository
import com.example.tabletop.utils.Helpers.viewModelOf
import com.example.tabletop.viewModels.PostViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var postViewModel: PostViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        postViewModel = viewModelOf(PostRepository()) as PostViewModel

        postViewModel.getPost()

        postViewModel.response.observe(this, { response ->
            if (response.isSuccessful) {
                Log.d("Response", response.body()?.userId.toString())
                Log.d("Response", response.body()?.id.toString())
                testTextView.text = response.body()?.title!!
                Log.d("Response", response.body()?.title!!)
                Log.d("Response", response.body()?.body!!)
            } else {
                Log.d("Response", response.errorBody().toString())
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