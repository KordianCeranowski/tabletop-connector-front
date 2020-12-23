package com.example.tabletop.main.fragment

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import com.example.tabletop.R
import com.example.tabletop.databinding.FragmentProfileBinding
import com.example.tabletop.mvvm.model.Event
import net.alexandroid.utils.mylogkt.logI

class ProfileFragment : BaseFragment(R.layout.fragment_profile) {

    override val binding: FragmentProfileBinding by viewBinding()

}