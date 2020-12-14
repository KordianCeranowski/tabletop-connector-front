package com.example.tabletop.main.activity

import android.os.Bundle
import android.viewbinding.library.activity.viewBinding
import com.example.tabletop.databinding.ActivityEventFormBinding

class EventFormActivity : BaseActivity() {

    override val binding: ActivityEventFormBinding by viewBinding()

    //todo
    //  weird behavior:
    //    layout will show up only if binding or it's members are mentioned (i.e. binding)?
    override fun setup() {
        binding
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setup()


        /*
           eventViewModel.save(getMockEvent())

           eventViewModel.responseOne.observe(this, { response ->
               if (response.isSuccessful) {
                   logD(response.getFullResponse())
                   if (response.code() == 201){
                       //działa
                   } else {
                       //nie działa
                   }
               } else {
                   logE(response.getFullResponse())
               }
           })
           */
    }
}