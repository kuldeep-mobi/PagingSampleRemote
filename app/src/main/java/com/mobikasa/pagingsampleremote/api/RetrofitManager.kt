package com.mobikasa.pagingsampleremote.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitManager {

    companion object {
        private const val BASE_URL = "https://api.themoviedb.org/3/movie/"
        private val httpLoggingInterceptor: HttpLoggingInterceptor by lazy {
            HttpLoggingInterceptor()
        }


        private val okHttpClient: OkHttpClient by lazy {
            OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor).build()
        }

        private val retrofit: Retrofit by lazy {
            Retrofit.Builder().client(okHttpClient).baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        fun getService(): APIService {
            return Retrofit.Builder().baseUrl(BASE_URL)
                .client(OkHttpClient.Builder().also { client ->
                    val logger = HttpLoggingInterceptor()
                    logger.level = HttpLoggingInterceptor.Level.BASIC
                    client.addInterceptor(logger)
                }.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(APIService::class.java)
        }
    }
}