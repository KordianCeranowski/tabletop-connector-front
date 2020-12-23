package com.example.tabletop.main.activity

import android.os.Bundle
import android.viewbinding.library.activity.viewBinding
import com.example.tabletop.databinding.ActivityEventEditBinding
import com.example.tabletop.mvvm.repository.EventRepository
import com.example.tabletop.mvvm.viewmodel.EventViewModel
import dev.ajkueterman.lazyviewmodels.lazyViewModels

class EventEditActivity : BaseActivity() {

    override val binding: ActivityEventEditBinding by viewBinding()

    override fun setup() {
        TODO("Not yet implemented")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setup()
    }
}