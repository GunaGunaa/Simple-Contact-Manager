package com.example.jetpackcompose.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class ContactResponse(
    val count: Int,
    val all_users: List<User>
)
@Parcelize
data class User(
    val id: Int,
    val email: String,
    val created_at: String,
    val updated_at: String,
    val first_name: String,
    val last_name: String,
    val gender: String?,
    val phone_number: String,
    val issued_at: String?,
    val dob: String?,
    val avatar: String?,
    val address: String
):Parcelable




