package com.example.tabletop.activity

import android.os.Bundle
import android.viewbinding.library.activity.viewBinding
import com.example.tabletop.databinding.ActivityEventBinding
import com.example.tabletop.model.Event

class EventActivity : BaseActivity() {

    override val binding: ActivityEventBinding by viewBinding()

    override fun setup() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setup()

        val passedEvent = intent.getSerializableExtra("EVENT") as Event
        binding.tvEventName.text = passedEvent.name
        binding.tvEventGames.text = passedEvent.creator
        binding.tvEventAddress.text = passedEvent.date
    }











}