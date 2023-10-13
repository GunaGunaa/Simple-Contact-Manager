package com.example.jetpackcompose.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class UserResponse(
    val Tokens: TokensData,
    val User: UserData
)

data class TokensData(
    val Auth_token: String,
    val Refresh_token: String
)

@Parcelize
data class UserData(
    val id: Int,
    val email: String,
    val first_name: String,
    val last_name: String,
    val gender: String?,
    val phone_number: String,
    val dob: String?,
    val avatar: String?,
    val address: String
):Parcelable








