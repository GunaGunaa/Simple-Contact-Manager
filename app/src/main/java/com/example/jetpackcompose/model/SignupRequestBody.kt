package com.example.jetpackcompose.model

data class SignupRequestBody(
    val user: SignupUser
)

data class SignupUser(
    val email: String,
    val first_name: String,
    val last_name: String,
    val phone_number: String,
    val address: String,
    val password: String
)
data class SignUpResponse(val message:String)
