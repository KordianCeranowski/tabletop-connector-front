package com.example.tabletop.activity

import android.os.Bundle
import android.viewbinding.library.activity.viewBinding
import android.widget.EditText
import android.widget.TextView
import com.example.tabletop.databinding.ActivityEventFormBinding
import com.example.tabletop.util.Helpers.getClassName
import com.livinglifetechway.k4kotlin.core.value
import net.alexandroid.utils.mylogkt.logI

class EventFormActivity : BaseActivity() {

    override val binding: ActivityEventFormBinding by viewBinding()

    //todo:
    // weird behavior: layout wont show up if some element is not mentioned?
    override fun setup() {
        logI("Starting ${getClassName()}")
        binding
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setup()
    }
}