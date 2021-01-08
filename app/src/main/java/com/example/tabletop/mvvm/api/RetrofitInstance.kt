package com.example.tabletop.mvvm.api

import com.example.tabletop.util.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitInstance {
    private val retrofit by lazy {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.HEADERS)

        val httpClient = OkHttpClient.Builder()
        //httpClient.addInterceptor(logging)

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build()
    }

    val userApi: UserApi by lazy { retrofit.create(UserApi::class.java) }

    val eventApi: EventApi by lazy { retrofit.create(EventApi::class.java) }

    val gameApi: GameApi by lazy { retrofit.create(GameApi::class.java) }
}