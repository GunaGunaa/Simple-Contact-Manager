package com.example.jetpackcompose.network

import android.content.Context
import android.content.SharedPreferences
import com.example.jetpackcompose.model.User
import com.example.jetpackcompose.model.UserData
import com.google.gson.Gson

class CASharedPreference(context: Context) {
    private val refreshToken = "refresh_token"
    private val authToken = "auth_token"
    private val userId = "user_id"
    private val userDetails = "user_data"
    private val sharedPreferences = context.getSharedPreferences("PREFS_NAME", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    fun setAuthToken(token: String) {
        editor.putString(authToken, token)
        editor.apply()
    }

    fun getAuthToken(): String? {
        return sharedPreferences.getString(authToken, "")
    }

    fun setRefreshToken(token: String) {
        editor.putString(refreshToken, token)
        editor.apply()
    }

    fun getRefreshToken(): String? {
        return sharedPreferences.getString(refreshToken, "")
    }

    fun setUserId(id: String) {
        editor.putString(userId, id)
        editor.apply()
    }

    fun getUserId(): String? {
        return sharedPreferences.getString(userId, "")
    }

    fun setUserData(userData: UserData) {
        val gson = Gson()
        var user = gson.toJson(userData)
        editor.putString(userDetails, user)
        editor.apply()
    }

    fun getUserData(): UserData {
        val gson = Gson()
        var jsonString = sharedPreferences.getString(userDetails, "")
        return gson.fromJson(jsonString, UserData::class.java)
    }
}