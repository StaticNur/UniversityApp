package com.example.universityapp.data.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    val mrsuApi:MrsuApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://papi.mrsu.ru/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MrsuApi::class.java)
    }
    val mrsuAuthApi:MrsuApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://p.mrsu.ru/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MrsuApi::class.java)
    }
}