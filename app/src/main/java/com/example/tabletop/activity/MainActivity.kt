package com.example.tabletop.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tabletop.R
import com.example.tabletop.adapter.MockAdapter
import com.example.tabletop.databinding.ActivityMainBinding
import com.example.tabletop.databinding.ActivityRegisterBinding
import com.example.tabletop.databinding.ActivityUserLoggedOutBinding
import com.example.tabletop.repository.MockRepository
import com.example.tabletop.repository.UserRepository
import com.example.tabletop.util.Helpers.logIt
import com.example.tabletop.util.Helpers.showToast
import com.example.tabletop.util.Helpers.viewModelOf
import com.example.tabletop.viewmodel.MockViewModel
import com.example.tabletop.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var mockViewModel: MockViewModel

    private val mockAdapter by lazy { MockAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setup()

        mockViewModel.getCustomPosts(2,"id","desc")

        mockViewModel.responseMany.observe(this) { response ->
            if (response.isSuccessful) {
                response.body()?.let { mockAdapter.setData(it) }
            } else {
               showToast(response.code())
            }
        }

        // mockViewModel.getPost()
        //
        // mockViewModel.responseSingle.observe(this, { response ->
        //     if (response.isSuccessful) {
        //         response.body()?.run {
        //             logIt(
        //                 "User ID: $userId",
        //                 "Title: $title",
        //                 "Body: $body"
        //             )
        //             // testTextView.text = title
        //         }
        //     } else {
        //         logIt(response.errorBody())
        //         // testTextView.text = response.code().toString()
        //     }
        // })

        /*
        todo:
          splash screen
          sidebar
        */
    }
    private fun setup() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mockViewModel = viewModelOf(MockRepository) as MockViewModel

        //RECYCLER VIEW
        recyclerView.adapter = mockAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }
}