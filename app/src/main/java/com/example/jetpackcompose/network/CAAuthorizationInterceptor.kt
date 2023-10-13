package com.example.jetpackcompose.network

import android.content.ContentValues.TAG
import android.util.Log
import com.example.jetpackcompose.CAApplication.Companion.sharedPreferences
import okhttp3.Interceptor
import okhttp3.Response
class CAAuthorizationInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .header("Authorization", "Bearer ${sharedPreferences.getAuthToken()}")
            .header("Refresh", sharedPreferences.getRefreshToken()!!)
            .build()
        Log.d(TAG, "interceptorAuthorization:${sharedPreferences.getAuthToken()} ")
        return chain.proceed(request)
    }
}