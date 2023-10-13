package com.example.jetpackcompose.viewmodel

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpackcompose.CAApplication
import com.example.jetpackcompose.CAConstants
import com.example.jetpackcompose.R
import com.example.jetpackcompose.`interface`.CAText
import com.example.jetpackcompose.model.User
import com.example.jetpackcompose.model.UserData
import com.example.jetpackcompose.model.UserResponse
import com.example.jetpackcompose.repository.CAContactRepository
import com.example.jetpackcompose.validation.Validation
import com.google.firebase.perf.FirebasePerformance
import com.google.firebase.perf.metrics.Trace
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class LoginViewModel @Inject constructor(private var repository: CAContactRepository) :
    ViewModel() {
    var loginUiState by mutableStateOf(LoginState())
    var passwordVisibility by mutableStateOf(false)
    var forgotPasswordUiStates by mutableStateOf(ForgotPassword())
    private val myTrace: Trace = FirebasePerformance.getInstance().newTrace("my_custom_trace")

    @SuppressLint("StaticFieldLeak")
    var context = CAApplication.contextApplication

    @Inject
    @Named("Guna")
    lateinit var name: String

    @Inject
    @Named("Guna1")
    lateinit var name1: String

    /**
     * To validate login page and store finally validation true
     */

    fun validation() {
        myTrace.start()
        //  Toast.makeText(context, name + name1, Toast.LENGTH_SHORT).show()
        loginUiState = if (loginUiState.userName.isEmpty()) {
            loginUiState.copy(userNameError = CAText.StringResource(R.string.enter_the_user_name))
        } else if (!Validation.isValidEmail(loginUiState.userName)) {
            loginUiState.copy(mailInvalidError = CAText.StringResource(R.string.enter_the_valid_mail_id))
        } else if (loginUiState.password.isEmpty()) {
            loginUiState.copy(passwordError = CAText.StringResource(R.string.enter_the_password))
        } else if (!Validation.isValidPassword(loginUiState.password)) {
            loginUiState.copy(passwordInvalidError = CAText.StringResource(R.string.password_format_error))
        } else loginUiState.copy(validation = true)
        myTrace.stop()
    }

    /**
     * To validate email for send otp
     */
    fun validateEmail() {
        loginUiState = if (loginUiState.forgotPasswordEmail.isEmpty()) {
            loginUiState.copy(emailError = context.getString(R.string.please_enter_otp))
        } else if (!Validation.isValidEmail(loginUiState.forgotPasswordEmail)) {
            loginUiState.copy(mailInvalidError = CAText.StringResource(R.string.enter_valid_mail_address))
        } else {
            loginUiState.copy(forgotPasswordEmail = loginUiState.forgotPasswordEmail)
            loginUiState.copy(forgotPasswordValidation = true)
        }
    }

    /**
     * to validate for forgot password otp and send the password and confirm password to api
     */
    fun forgotPasswordValidation() {
        if (forgotPasswordUiStates.otp.isEmpty() || forgotPasswordUiStates.password.isEmpty() || forgotPasswordUiStates.confirmPassword.isEmpty()) {
            forgotPasswordUiStates =
                forgotPasswordUiStates.copy(forgotPasswordFieldValidation = true)
        } else if (!Validation.isValidPassword(forgotPasswordUiStates.password)) {
            forgotPasswordUiStates =
                forgotPasswordUiStates.copy(invalidPassword = context.getString(R.string.password_format_error))
        } else if (!Validation.isPasswordMatching(
                forgotPasswordUiStates.password,
                forgotPasswordUiStates.confirmPassword
            )
        ) {
            forgotPasswordUiStates =
                forgotPasswordUiStates.copy(passwordMatching = context.getString(R.string.password_shoud_be_match))
        } else {
            forgotPasswordUiStates = forgotPasswordUiStates.copy(validationSuccess = true)
        }
    }

    fun setForgotPasswordOtp(otp: String) {
        forgotPasswordUiStates = forgotPasswordUiStates.copy(otp = otp)
    }

    fun setForgotPasswordEmail(email: String) {
        loginUiState = loginUiState.copy(forgotPasswordEmail = email)
    }

    fun setUsername(name: String) {
        loginUiState = loginUiState.copy(userName = name)
    }

    fun setPassword(password: String) {
        loginUiState = loginUiState.copy(password = password)
    }

    fun setDialogState() {
        loginUiState = loginUiState.copy(sendEmailDialog = true)
    }

    fun setDialogStateFalse() {
        loginUiState = loginUiState.copy(sendEmailDialog = false)
    }

    fun setForgotPasswordOtpTrue() {
        forgotPasswordUiStates = forgotPasswordUiStates.copy(forgotPasswordOtpDialog = true)
    }

    fun setForgotConfirmPassword(confirmPassword: String) {
        forgotPasswordUiStates = forgotPasswordUiStates.copy(confirmPassword = confirmPassword)
    }

    fun setForgotPassword(password: String) {
        forgotPasswordUiStates = forgotPasswordUiStates.copy(password = password)
    }

    fun loginValidation() {
        loginUiState = loginUiState.copy(isLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
            loginUiState = loginUiState.copy(validation = false, isErrorMessage = null)
            when (val response =
                repository.userLogin(loginUiState.userName, loginUiState.password)) {
                is UserResponse -> {
                    val sharedPreferences = CAApplication.sharedPreferences
                    sharedPreferences.setAuthToken(response.Tokens.Auth_token)
                    sharedPreferences.setRefreshToken(response.Tokens.Refresh_token)
                    sharedPreferences.setUserId(response.User.email)
                    sharedPreferences.setUserData(response.User)
                    loginUiState = loginUiState.copy(
                        token = response.Tokens.Auth_token,
                        isLoading = false,
                        isLoginSuccess = true,
                        userData = response.User
                    )
                }
                is Int -> {
                    loginUiState =
                        if (response == 401) loginUiState.copy(
                            isErrorMessage = CAText.StringResource(R.string.user_name_or_password_is_wrong),
                            isLoading = false
                        )
                        else loginUiState.copy(
                            isErrorMessage = CAText.StringResource(R.string.could_not_connect_to_the_service),
                            isLoading = false
                        )
                }
            }
        }
    }

    /**
     * api call for to send otp in forgot password dialog
     */

    fun forgotPassword() {
        loginUiState = loginUiState.copy(isLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.forgotPassword(loginUiState.forgotPasswordEmail)
            loginUiState = loginUiState.copy(isLoading = false, openDialog = false)
            Log.d(TAG, "forgotPassword:$response ")
            loginUiState = if (response == 200) loginUiState.copy(forgotPasswordResponse = true)
            else loginUiState.copy(forgotPasswordEmailFailure = true)
        }
    }

    fun setNewPassword() {
        forgotPasswordUiStates = forgotPasswordUiStates.copy(isLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.setNewPassword(
                forgotPasswordUiStates.otp,
                forgotPasswordUiStates.password,
                forgotPasswordUiStates.confirmPassword
            )
            forgotPasswordUiStates = if (response == 200) {
                forgotPasswordUiStates.copy(
                    isLoading = false,
                    forgotPasswordSetPasswordResponse = true
                )
            } else {
                forgotPasswordUiStates.copy(errorMessage = context.getString(R.string.something_went_wrong))
            }
        }
    }
}

data class LoginState(
    var passwordError: CAText? = null,
    var userNameError: CAText? = null,
    var mailInvalidError: CAText? = null,
    var passwordInvalidError: CAText? = null,
    val isLoading: Boolean = false,
    val isLoginSuccess: Boolean = false,
    val isErrorMessage: CAText? = null,
    val password: String = "",
    var userName: String = "",
    val token: String = "",
    val validation: Boolean = false,
    var sendEmailDialog: Boolean = false,
    var forgotPasswordEmail: String = "",
    var emailError: String = "",
    var forgotPassword: Boolean = false,
    var forgotPasswordResponse: Boolean = false,
    var forgotPasswordValidation: Boolean = false,
    var forgotPasswordEmailFailure: Boolean = false,
    var openDialog: Boolean = true,
    var userData:UserData?= null
)

data class ForgotPassword(
    var isLoading: Boolean = false,
    var openDialog: Boolean = true,
    val otp: String = "",
    var forgotPasswordOtpDialog: Boolean = false,
    var password: String = "",
    var confirmPassword: String = "",
    var forgotPasswordFieldValidation: Boolean = false,
    var passwordMatching: String = "",
    var invalidPassword: String = "",
    var validationSuccess: Boolean = false,
    var forgotPasswordSetPasswordResponse: Boolean = false,
    var errorMessage: String = ""
)