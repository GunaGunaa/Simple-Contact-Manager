package com.example.jetpackcompose.model

data class ResetPasswordRequest(val user:UserDetails)
data class UserDetails(val password:String,val current_password:String)