package com.example.jetpackcompose.repository

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.util.Log
import com.example.jetpackcompose.R
import com.example.jetpackcompose.model.ResetPasswordRequest
import com.example.jetpackcompose.model.SignupRequestBody
import com.example.jetpackcompose.network.CAServiceBuilder
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class CAContactRepository(serviceBuilder: CAServiceBuilder) {
    private val contactService = serviceBuilder.unAuthorizedClient()
    private val authorizedContactService = serviceBuilder.authorizedClient()

    suspend fun signUp(requestBody: SignupRequestBody): Any {
        return try {
            val response = contactService.signUp(requestBody)
            if (response.isSuccessful) {
                response.body()!!
            } else if (response.code() == 401) R.string.email_or_mobile_number_already_taken
            else R.string.could_not_connect_to_the_service
        } catch (exception: Exception) {
            return R.string.could_not_connect_to_the_service
        }
    }

    suspend fun sendSignupOTP(confirmationToken: String): Any {
        return try {
            val response = contactService.sendSignUpOTP(confirmationToken)
            if (response.isSuccessful) response.body()!!
            else if (response.code() == 400) R.string.account_is_already_verified
            else R.string.could_not_connect_to_the_service
        } catch (exception: Exception) {
            return R.string.could_not_connect_to_the_service
        }
    }

    suspend fun userLogin(userName: String, password: String): Any {
        return try {
            val response = contactService.userLogin(userName, password)
            if (response.isSuccessful) {
                response.body()!!
            } else {
                response.code()
            }
        } catch (e: Exception) {
            Log.d(TAG, "userLogin:$e ")
        }

    }

    suspend fun getContactList(currentPage: Int): Flow<Any> {
        return flow {
            try {
                val response = authorizedContactService.getContactList(currentPage)
                if (response.isSuccessful) {
                    emit(response.body()!!.all_users)
                } else {
                    when (response.code()) {
                        401 -> emit(R.string.expired)
                        else -> emit(R.string.server_error)
                    }
                }
            } catch (e: Exception) {
                emit(401)
            }
        }
    }


    suspend fun forgotPassword(email: String): Int {
        return try {
            val response = contactService.forgotPassword(email)
            if (response.isSuccessful) {
                Log.d(TAG, "forgotPasswordRepository:${response.body()!!.message} ")
                200
            } else {
                401
            }
        } catch (e: Exception) {
            404
        }
    }

    suspend fun setNewPassword(token: String, password: String, confirmPassword: String): Int {
        return try {
            val response = contactService.setNewPassword(token, password, confirmPassword)
            if (response.isSuccessful) {
                200
            } else {
                401
            }
        } catch (e: Exception) {
            404
        }
    }

    suspend fun resetPassword(resetPasswordRequest: ResetPasswordRequest): Any {
        return try {
            val response = authorizedContactService.resetPassword(resetPasswordRequest)
            if (response.isSuccessful) {
                response.body()!!
            } else {
                response.code()
            }
        } catch (exception: Exception) {
            return 501
        }

    }
}