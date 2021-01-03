package com.example.tabletop.mvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tabletop.util.SingleLiveEvent
import kotlinx.coroutines.launch
import net.alexandroid.utils.mylogkt.logV

class BottomNavBarViewModel : ViewModel() {

    val isChatEnabled = SingleLiveEvent<Boolean>().apply { value = false }

    fun setChatEnabled(value: Boolean) {
        isChatEnabled.value = value
    }
}