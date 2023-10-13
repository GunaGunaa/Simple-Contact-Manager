package com.example.jetpackcompose.network

import android.annotation.SuppressLint
import com.example.jetpackcompose.CAConstants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class CAServiceBuilder {
    private val url = CAConstants.BASE_URL
    private val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    @SuppressLint("SuspiciousIndentation")
    fun unAuthorizedClient(): CAContactService {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS).addInterceptor(logger)
        val builder = Retrofit.Builder().baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient.build())
        val retrofit = builder.build()
        return retrofit.create(CAContactService::class.java)
    }

    @SuppressLint("SuspiciousIndentation")
    fun authorizedClient(): CAContactService {
        val okHttpClient = OkHttpClient.Builder().addInterceptor(logger)
            .addInterceptor(CAAuthorizationInterceptor())
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
        val builder = Retrofit.Builder().baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient.build())
        val retrofit = builder.build()
        return retrofit.create(CAContactService::class.java)
    }
}

