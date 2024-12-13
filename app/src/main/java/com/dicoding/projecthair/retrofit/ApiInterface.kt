package com.dicoding.projecthair.retrofit


import com.dicoding.projecthair.response.HairstyleResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

object RetrofitInstance {
    private const val BASE_URL = "https://hairmuseimg2-325820985735.asia-southeast2.run.app"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: HairstyleApiService by lazy {
        retrofit.create(HairstyleApiService::class.java)
    }
}