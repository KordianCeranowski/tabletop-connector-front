package com.example.tabletop.mvvm.viewmodel

interface IViewModelSave<T> {
     fun save(auth: String, model: T)
}