package com.example.jetpackcompose.viewmodel

import android.annotation.SuppressLint
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpackcompose.CAApplication
import com.example.jetpackcompose.R
import com.example.jetpackcompose.`interface`.CAText
import com.example.jetpackcompose.model.SendOtpResponse
import com.example.jetpackcompose.model.SignUpResponse
import com.example.jetpackcompose.model.SignupRequestBody
import com.example.jetpackcompose.model.SignupUser
import com.example.jetpackcompose.repository.CAContactRepository
import com.example.jetpackcompose.validation.Validation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(private var repository: CAContactRepository) :
    ViewModel() {
    var signupUiState by mutableStateOf(SignUpState())

    @SuppressLint("StaticFieldLeak")
    val context = CAApplication.contextApplication
    fun signUpValidation() {
        if (signupUiState.firstName.isEmpty())
            signupUiState =
                signupUiState.copy(firstNameError = CAText.StringResource(R.string.enter_the_first_name))
        if (signupUiState.lastName.isEmpty()) signupUiState =
            signupUiState.copy(lastNameError = CAText.StringResource(R.string.enter_the_lastname))
        if (signupUiState.company.isEmpty()) signupUiState =
            signupUiState.copy(companyError = CAText.StringResource(R.string.enter_the_company_name))
        if (signupUiState.email.isEmpty()) signupUiState =
            signupUiState.copy(emailError = CAText.StringResource(R.string.enter_the_mail))
        if (!Validation.isValidEmail(signupUiState.email))
            signupUiState =
                signupUiState.copy(emailFormatError = CAText.StringResource(R.string.enter_the_valid_mail_id))
        if (signupUiState.phone.isEmpty()) signupUiState =
            signupUiState.copy(phoneError = CAText.StringResource(R.string.enter_the_phone_number))
        if (!Validation.isValidNumber(signupUiState.phone))
            signupUiState =
                signupUiState.copy(numberFormatError = CAText.StringResource(R.string.enter_valid_mobile_number))
        if (signupUiState.password.isEmpty()) signupUiState =
            signupUiState.copy(passwordError = CAText.StringResource(R.string.enter_the_password))
        if (!Validation.isValidPassword(signupUiState.password))
            signupUiState =
                signupUiState.copy(passwordFormatError = CAText.StringResource(R.string.password_format_error))
        if (signupUiState.confirmPassword.isEmpty()) signupUiState =
            signupUiState.copy(confirmPasswordError = CAText.StringResource(R.string.enter_the_confirm_password))
        if (!Validation.isPasswordMatching(
                signupUiState.confirmPassword,
                signupUiState.password
            )
        )
            signupUiState =
                signupUiState.copy(passwordMatchingError = CAText.StringResource(R.string.password_shoud_be_match))
        if (signupUiState.street.isEmpty()) signupUiState =
            signupUiState.copy(streetError = CAText.StringResource(R.string.enter_the_street_name))
        if (signupUiState.city.isEmpty()) signupUiState =
            signupUiState.copy(cityError = CAText.StringResource(R.string.enter_the_city_name))
        if (signupUiState.postalCode.isEmpty()) signupUiState =
            signupUiState.copy(postalCodeError = CAText.StringResource(R.string.enter_the_postal_code))
        if (signupUiState.state.isEmpty()) signupUiState =
            signupUiState.copy(stateError = CAText.StringResource(R.string.enter_the_state_name))

        signupUiState.apply {
            if (firstName.isNotEmpty() && lastName.isNotEmpty() && company.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() &&
                confirmPassword.isNotEmpty() && phone.isNotEmpty() && street.isNotEmpty() && state.isNotEmpty() && city.isNotEmpty() && postalCode.isNotEmpty()
                && Validation.isValidEmail(email) && Validation.isValidPassword(password) && Validation.isPasswordMatching(
                    confirmPassword,
                    password
                ) && Validation.isValidNumber(phone)
            ) {
                signupUiState = signupUiState.copy(validationSuccess = true)
            }
        }
    }

    fun setFirstName(firstName: String) {
        signupUiState = signupUiState.copy(firstName = firstName)
    }

    fun setLastName(lastName: String) {
        signupUiState = signupUiState.copy(lastName = lastName)
    }

    fun setCompany(company: String) {
        signupUiState = signupUiState.copy(company = company)
    }

    fun setPhone(phone: String) {
        signupUiState = signupUiState.copy(phone = phone)
    }

    fun setPassword(password: String) {
        signupUiState = signupUiState.copy(password = password)
    }

    fun setConfirmPassword(confirmPassword: String) {
        signupUiState = signupUiState.copy(confirmPassword = confirmPassword)
    }

    fun setStreetName(Street: String) {
        signupUiState = signupUiState.copy(street = Street)
    }

    fun setCityName(city: String) {
        signupUiState = signupUiState.copy(city = city)
    }

    fun setStateName(State: String) {
        signupUiState = signupUiState.copy(state = State)
    }

    fun setPostalCode(it: String) {
        signupUiState = signupUiState.copy(postalCode = it)
    }

    fun setMail(mail: String) {
        signupUiState = signupUiState.copy(email = mail)
    }

    fun userSignUp() {
        signupUiState = signupUiState.copy(isLoading = true)
        val address =
            signupUiState.street + "," + signupUiState.city + "," + signupUiState.state + "," + signupUiState.postalCode
        viewModelScope.launch {
            val requestBody = SignupRequestBody(
                SignupUser(
                    signupUiState.email,
                    signupUiState.firstName,
                    signupUiState.lastName,
                    signupUiState.phone,
                    address,
                    signupUiState.password
                )
            )
            when (val response = repository.signUp(requestBody = requestBody)) {
                is SignUpResponse -> signupUiState =
                    signupUiState.copy(responseSuccess = response.message, isLoading = false)
                is Int -> signupUiState =
                    signupUiState.copy(
                        responseFailure = CAText.StringResource(response),
                        isLoading = false
                    )
            }
        }
    }

    fun setSignupOTP(otp: String) {
        signupUiState = signupUiState.copy(signupOTP = otp)
    }

    fun otpValidation() {
        signupUiState =
            if (signupUiState.signupOTP.isEmpty()) signupUiState.copy(
                signupOTPError = CAText.StringResource(
                    R.string.please_enter_otp
                )
            )
            else signupUiState.copy(signupOtpValidation = true)

    }

    fun sendSignUpOTP() {
        signupUiState = signupUiState.copy(isLoading = true)
        viewModelScope.launch {
            when (val response = repository.sendSignupOTP(signupUiState.signupOTP)) {
                is SendOtpResponse -> signupUiState = signupUiState.copy(
                    signUpOtpSuccessResponse = response.Message,
                    isLoading = false
                )
                is Int -> signupUiState =
                    signupUiState.copy(
                        signupOtpFailureResponse = CAText.StringResource(response),
                        isLoading = false
                    )
            }
        }
    }

}

data class SignUpState(
    var firstName: String = "",
    var lastName: String = "",
    var company: String = "",
    var email: String = "",
    var phone: String = "",
    var password: String = "",
    var confirmPassword: String = "",
    var street: String = "",
    var city: String = "",
    var state: String = "",
    var postalCode: String = "",
    var firstNameError: CAText? = null,
    var lastNameError: CAText? = null,
    var companyError: CAText? = null,
    var emailError: CAText? = null,
    var phoneError: CAText? = null,
    var passwordError: CAText? = null,
    var confirmPasswordError: CAText? = null,
    var streetError: CAText? = null,
    var cityError: CAText? = null,
    var stateError: CAText? = null,
    var postalCodeError: CAText? = null,
    val validationSuccess: Boolean = false,
    var numberFormatError: CAText? = null,
    var emailFormatError: CAText? = null,
    var passwordMatchingError: CAText? = null,
    var passwordFormatError: CAText? = null,
    var isLoading: Boolean = false,
    var responseSuccess: String = "",
    var responseFailure: CAText? = null,
    var otpDialog: Boolean = false,
    var signupOTP: String = "",
    var signupOTPError: CAText? = null,
    var signUpOtpSuccessResponse: String = "",
    var signupOtpFailureResponse: CAText? = null,
    var signupOtpValidation: Boolean = false,
)