package com.example.seccouncil.network

import android.content.Context
import android.util.Log
import com.example.seccouncil.utlis.DataStoreManger
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
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

    /**
     * Builds an OkHttpClient that attaches the JWT token to every request using an interceptor.
     * This method shows how you can pull the token from DataStore (via DataStoreManger).
     */
    /**
     * Builds an OkHttpClient that attaches the JWT token dynamically using an interceptor.
     */
    private fun createAuthenticatedOkHttpClient(context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val original = chain.request()
                val token = runBlocking {
                    DataStoreManger(context).getStoredToken().firstOrNull()  // Collect first emitted value
                }
                Log.i("tokennnn","$token")
                val requestBuilder = original.newBuilder()
                if (!token.isNullOrEmpty()) {
                    requestBuilder.header("Authorization", "Bearer $token")
                }
                chain.proceed(requestBuilder.build())
            }
            .build()
    }

    /**
     * Creates a Retrofit instance that uses the authenticated OkHttpClient.
     * Call this method when you need an instance of ApiInterface that automatically adds your JWT token.
     */
    fun createAuthenticatedApi(context: Context): ApiInterface {
        val client = createAuthenticatedOkHttpClient(context)
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)
    }

}