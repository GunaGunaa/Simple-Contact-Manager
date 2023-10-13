package com.example.jetpackcompose.network

import com.example.jetpackcompose.model.*
import retrofit2.Response
import retrofit2.http.*

interface CAContactService {
    @POST("users")
    @Headers("Accept:application/json")
    suspend fun signUp(@Body signUpRequestBody: SignupRequestBody): Response<SignUpResponse>

    @GET("users/confirmation")
    @Headers("Accept:application/json")
    suspend fun sendSignUpOTP(@Query("confirmation_token") confirmationToken: String): Response<SendOtpResponse>

    @FormUrlEncoded
    @POST("users/sign_in")
    @Headers("Accept:application/json")
    suspend fun userLogin(
        @Field("user[login]") userName: String,
        @Field("user[password]") password: String
    ): Response<UserResponse>

    @GET("home")
    @Headers("Accept:application/json")
    suspend fun getContactList(@Query("page") page: Int): Response<ContactResponse>

    @FormUrlEncoded
    @POST("users/password")
    @Headers("Accept:application/json")
    suspend fun forgotPassword(@Field("user[email]") email: String): Response<ForgotPasswordResponse>

    @FormUrlEncoded
    @PUT("users/password")
    @Headers("Accept:application/json")
    suspend fun setNewPassword(
        @Field("user[reset_password_token]") userName: String,
        @Field("user[password]") password: String,
        @Field("user[password_confirmation]") confirmPassword: String
    ): Response<String>

    @PUT("users/change_password")
    @Headers("Accept:application/json")
    suspend fun resetPassword(
        @Body resetPasswordRequest: ResetPasswordRequest
    ): Response<String>
}