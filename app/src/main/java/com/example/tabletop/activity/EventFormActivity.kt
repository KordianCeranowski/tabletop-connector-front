package com.example.tabletop.activity

import android.os.Bundle
import android.viewbinding.library.activity.viewBinding
import com.example.tabletop.databinding.ActivityEventFormBinding

class EventFormActivity : BaseActivity() {

    override val binding: ActivityEventFormBinding by viewBinding()

    override fun setup() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setup()

    }
}