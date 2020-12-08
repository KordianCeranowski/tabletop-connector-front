package com.example.tabletop.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tabletop.databinding.ActivityEventFormBinding

class EventFormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEventFormBinding

    private fun setup() {
        binding = ActivityEventFormBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setup()

    }
}