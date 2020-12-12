package com.example.tabletop.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tabletop.model.Model
import com.example.tabletop.model.helpers.Many
import retrofit2.Response

abstract class BaseViewModel<T : Model> : ViewModel() {

    val responseOne = MutableLiveData<Response<T>>()

    val responseMany = MutableLiveData<Response<Many<T>>>()

    abstract fun getMany(sort: String, order: String)

    abstract fun getMany(options: Map<String, String>)

    abstract fun save(model: T)

    abstract fun getOne(id: String)

    abstract fun remove(id: String)

    abstract fun edit(id: String, newModel: T)
}