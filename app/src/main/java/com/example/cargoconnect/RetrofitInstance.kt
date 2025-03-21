package com.example.cargoconnect

// RetrofitInstance.kt
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://your-flask-api-url.com/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: APIService = retrofit.create(APIService::class.java)
}
