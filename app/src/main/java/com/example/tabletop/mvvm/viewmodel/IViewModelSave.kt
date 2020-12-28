package com.example.tabletop.mvvm.viewmodel

interface IViewModelSave<T> {
     fun save(accessToken: String, model: T)
}