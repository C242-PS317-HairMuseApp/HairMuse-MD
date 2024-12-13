package com.dicoding.projecthair.retrofit

import com.dicoding.projecthair.response.HairstyleResponse
import retrofit2.Response  // Ubah import ini
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface HairstyleApiService {
    @GET("hairstyles/{gender}/{face_shape}")
    suspend fun getHairstyles(
        @Path("gender") gender: String,
        @Path("face_shape") faceShape: String
    ): Response<HairstyleResponse>

    companion object {
        private const val BASE_URL = "https://hairmuseimg2-325820985735.asia-southeast2.run.app/"

        fun create(): HairstyleApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(HairstyleApiService::class.java)
        }
    }
}