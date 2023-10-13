package com.example.jetpackcompose.validation

object Validation {
    private val mobileNumberPattern = "^[6-9]\\d{9}$".toRegex()
    fun isValidEmail(email: String): Boolean {
        val emailPattern = Regex("[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
        return emailPattern.matches(email)
    }

    fun isValidPassword(password: String): Boolean {
        return password.length >= 5 && password.any(Char::isUpperCase) && password.any(Char::isDigit)
    }

    fun isValidNumber(phone: String): Boolean {
        return mobileNumberPattern.matches(phone)
    }

    fun isPasswordMatching(confirmPassword: String, password: String): Boolean {
        return password == confirmPassword
    }
}
