package com.example.tabletop.mvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tabletop.mvvm.model.Model
import com.example.tabletop.mvvm.model.helpers.Many
import com.example.tabletop.util.SingleLiveEvent
import retrofit2.Response

abstract class BaseViewModel<T : Model> : ViewModel() {

    val responseOne = SingleLiveEvent<Response<T>>()

    val responseMany = SingleLiveEvent<Response<Many<T>>>()

    abstract fun getMany(accessToken: String, options: Map<String, String> = emptyMap())

    abstract fun getOne(accessToken: String, id: String)

    abstract fun remove(accessToken: String, id: String)

    abstract fun edit(accessToken: String, id: String, newModel: T)
}