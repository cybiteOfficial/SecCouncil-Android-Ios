package com.example.seccouncil.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiService {
    private const val BASE_URL = "https://seccouncil.onrender.com" // Replace with your API base URL

    // Create Retrofit instance
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Expose ApiInterface to the app
    val api: ApiInterface by lazy {
        retrofit.create(ApiInterface::class.java)
    }
}