package com.example.jetpackcompose.viewmodel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpackcompose.model.ResetPasswordRequest
import com.example.jetpackcompose.model.UserDetails
import com.example.jetpackcompose.repository.CAContactRepository
import com.example.jetpackcompose.validation.Validation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(private var repository: CAContactRepository) :
    ViewModel() {
    var changePasswordUiState by mutableStateOf(ChangePassword())
    fun validation() {
        if (changePasswordUiState.currentPassword.isEmpty()) {
            changePasswordUiState =
                changePasswordUiState.copy(currentPasswordError = "Please enter current password")
        } else if (changePasswordUiState.password.isEmpty()) {
            changePasswordUiState =
                changePasswordUiState.copy(newPasswordError = "Please enter new password")
        } else if (!Validation.isValidPassword(password = changePasswordUiState.password)) {
            changePasswordUiState =
                changePasswordUiState.copy(validPassword = "Password should have number,capital letter")
        } else if (changePasswordUiState.confirmPassword.isEmpty()) {
            changePasswordUiState =
                changePasswordUiState.copy(confirmPasswordError = "please enter the confirm password")
        } else if (!Validation.isPasswordMatching(
                changePasswordUiState.password,
                changePasswordUiState.confirmPassword
            )
        ) {
            changePasswordUiState =
                changePasswordUiState.copy(passwordMatching = "Password should be match")
        } else {
            changePasswordUiState = changePasswordUiState.copy(fieldValidation = true)
        }
    }

    fun setCurrentPassword(currentPassword: String) {
        changePasswordUiState = changePasswordUiState.copy(currentPassword = currentPassword)
    }

    fun setNewPassword(newPassword: String) {
        changePasswordUiState = changePasswordUiState.copy(password = newPassword)
    }

    fun setConfirmPassword(confirmPassword: String) {
        changePasswordUiState = changePasswordUiState.copy(confirmPassword = confirmPassword)
    }

    fun resetPassword() {
        changePasswordUiState = changePasswordUiState.copy(isLoading = true)
        viewModelScope.launch {
            val resetPasswordRequest = ResetPasswordRequest(
                UserDetails(
                    changePasswordUiState.currentPassword,
                    changePasswordUiState.password
                )
            )
            when (val response = repository.resetPassword(resetPasswordRequest)) {
                is String -> changePasswordUiState =
                    changePasswordUiState.copy(resetPasswordResponse = true, isLoading = false)
                is Int -> {
                    if (response == 401) {
                        Log.d(TAG, "resetPassword: ")
                    } else
                        Log.d(TAG, "$response")
                }
            }
        }
    }

}

data class ChangePassword(
    var isLoading: Boolean = false,
    var currentPassword: String = "",
    var password: String = "",
    var confirmPassword: String = "",
    val fieldValidation: Boolean = false,
    var currentPasswordError: String = "",
    var newPasswordError: String = "",
    var confirmPasswordError: String = "",
    var validPassword: String = "",
    var passwordMatching: String = "",
    var resetPasswordResponse: Boolean = false
)