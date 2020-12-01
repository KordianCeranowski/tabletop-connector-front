package com.example.tabletop.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tabletop.R
import com.example.tabletop.adapter.MockAdapter
import com.example.tabletop.databinding.ActivityMainBinding
import com.example.tabletop.repository.MockRepository
import com.example.tabletop.util.Helpers.viewModelOf
import com.example.tabletop.util.runLoggingConfig
import com.example.tabletop.viewmodel.MockViewModel
import kotlinx.android.synthetic.main.activity_main.*
import splitties.toast.UnreliableToastApi
import splitties.toast.toast
import splitties.views.dsl.core.add

@UnreliableToastApi
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
               toast(response.code())
            }
        }

        /*
        mockViewModel.getPost()

        mockViewModel.responseSingle.observe(this, { response ->
            if (response.isSuccessful) {
                response.body()?.run {
                    logIt(
                        "User ID: $userId",
                        "Title: $title",
                        "Body: $body"
                    )
                    // testTextView.text = title
                }
            } else {
                logIt(response.errorBody())
                // testTextView.text = response.code().toString()
            }
         })
        */
    }

    private fun setup() {
        runLoggingConfig()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mockViewModel = viewModelOf(MockRepository) as MockViewModel

        //RECYCLER VIEW
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = mockAdapter
        }
    }
}