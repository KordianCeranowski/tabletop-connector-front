package com.example.tabletop.main.fragment

import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment(contentLayoutId: Int) : Fragment(contentLayoutId) {

    protected abstract val binding: ViewBinding
}