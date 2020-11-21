package com.example.tabletop.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.example.tabletop.R
import com.example.tabletop.repository.Repository
import com.example.tabletop.viewmodels.MainViewModel
import com.example.tabletop.viewmodels.MainViewModelFactory
import kotlinx.android.synthetic.main.activity_user_not_logged_in.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

        viewModel.getPost()
        viewModel.myResponse.observe(this, { response ->
            if (response.isSuccessful) {
                Log.d("Response", response.body()?.userId.toString())
                Log.d("Response", response.body()?.id.toString())
                textView.text = response.body()?.title!!
                Log.d("Response", response.body()?.title!!)
                Log.d("Response", response.body()?.body!!)
            } else {
                Log.d("Response", response.errorBody().toString())
                textView.text = response.code().toString()
            }
        })

        //todo splash screen
        //todo recycler view
        //todo sidebar
    }
}