package com.example.tabletop.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tabletop.databinding.ActivityEventBinding
import com.example.tabletop.model.Event

class EventActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEventBinding

    private fun setup() {
        binding = ActivityEventBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setup()

        val passedEvent = intent.getSerializableExtra("EVENT") as Event
        binding.tvEventName.text = passedEvent.name
    }











}