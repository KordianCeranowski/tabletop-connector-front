package com.example.tabletop.api

import com.example.tabletop.utils.Constants.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val simpleApi: SimpleApi by lazy {
        retrofit.create(SimpleApi::class.java)
    }

    val userApi: UserApi by lazy {
        retrofit.create(UserApi::class.java)
    }

    val eventApi: EventApi by lazy {
        retrofit.create(EventApi::class.java)
    }
}