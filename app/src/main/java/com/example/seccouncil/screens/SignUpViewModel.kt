package com.example.seccouncil.screens

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seccouncil.network.ApiService
import com.example.seccouncil.network.EmailRequest
import com.example.seccouncil.network.SignUpRequest
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class SignUpViewModel : ViewModel() {

    // UI States
    val otpSentMessage = mutableStateOf("")
    val signUpMessage = mutableStateOf("")
    val errorMessage = mutableStateOf("")
    val isSignUpSuccessful = mutableStateOf(false)
    val isOtpSent = mutableStateOf(false)
    val otp = mutableStateOf("")
    val isLoading = mutableStateOf(false)
    val navigateToOtpScreen = mutableStateOf(false)
    val validOtp = mutableStateOf(false)
    val shouldCloseApp = mutableStateOf(false)

    // Input Fields
    val email = mutableStateOf("")
    val password = mutableStateOf("")
    val confirmPassword = mutableStateOf("")
    val otpUser = mutableStateOf("")
    val firstname = mutableStateOf("")
    val lastname = mutableStateOf("")

    // Update Input Fields

    // Function to update OTP value
    fun onOtpUserChange(newOtp: String) {
        otpUser.value = newOtp
    }
    fun onFirstnameChange(newUsername: String) {
        firstname.value = newUsername
    }
    fun onLastnameChange(newUsername: String) {
        lastname.value = newUsername
    }

    fun onEmailChange(newEmail: String) {
        email.value = newEmail
    }

    fun onPasswordChange(newPassword: String) {
        password.value = newPassword
    }

    fun onConfirmPasswordChange(newConfirmPassword: String) {
        confirmPassword.value = newConfirmPassword
    }

    // Validation Functions
    private fun validateFields(): Boolean {
        val errors = mutableListOf<String>()

        if (firstname.value.trim().isEmpty()||lastname.value.trim().isEmpty() || email.value.trim().isEmpty() ||
            password.value.trim().isEmpty() || confirmPassword.value.trim().isEmpty()
        ) {
            errors.add("All fields are required.")
        }
        else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.value.trim()).matches()) {
            errors.add("Invalid email address.")
        }
        else if (password.value != confirmPassword.value) {
            errors.add("Passwords do not match.")
        }
        else if (!Regex("^(?=.*[A-Z])(?=.*[!@#\$%^&*(),.?\":{}|<>])(?=.*\\d)[A-Za-z\\d!@#\$%^&*(),.?\":{}|<>]{8,}\$")
                .matches(password.value)) {
            errors.add("Password must be at least 8 characters, contain an uppercase letter, special character, and number.")
        }

        return if (errors.isNotEmpty()) {
            errorMessage.value = errors.joinToString("\n")
            false
        } else {
            errorMessage.value = ""
            true
        }
    }


    fun validateOtp() {
        Log.e("otp", "${otp}= ${otpUser.value}")
        if (otp.value == otpUser.value) {
            errorMessage.value = ""
            viewModelScope.launch {
                isLoading.value = true
                registerUserApi()
                isLoading.value = false // remove it
//                if (isSignUpSuccessful.value) {
//                    validOtp.value = true
//                } else {
//                    errorMessage.value = "Registration failed. Please try again."
//                }
////                isLoading.value = false
            }
        } else {
            errorMessage.value = "Invalid OTP. Please check and try again."
        }
    }



    // Network Requests
    private suspend fun sendOtp(email: String) {
        try {
            val response = ApiService.api.sendOtp(EmailRequest(email))
            if (response.isSuccessful && response.body()?.success == true) {
                otpSentMessage.value = response.body()?.message.orEmpty()
                otp.value = response.body()?.otp.orEmpty()
                Log.e("check","OTP send is ${otp}")
                isOtpSent.value = true
            } else {
                errorMessage.value = "Failed to send OTP."
            }
        } catch (e: Exception) {
            handleNetworkError(e, "OTP sending failed.")
        }
    }

    private suspend fun registerUserApi() {
        try {
            val request = SignUpRequest(
                firstName = firstname.value,
                lastName = lastname.value,
                email = email.value,
                password = password.value,
                confirmPassword = confirmPassword.value,
                accountType = "Student",
                otp = otpUser.value
            )
            val response = ApiService.api.signUp(request)
            if (response.isSuccessful && response.body()?.success == true) {
                isSignUpSuccessful.value = true
                signUpMessage.value = response.body()?.message.orEmpty()
                validOtp.value = true // remove this
            } else {
                errorMessage.value = response.errorBody()?.string().orEmpty()
                isSignUpSuccessful.value = false
            }
        } catch (e: Exception) {
            handleNetworkError(e, "Sign-up failed.")
            isSignUpSuccessful.value = false
        }
    }

    private fun handleNetworkError(e: Exception, defaultMessage: String) {
        errorMessage.value = when (e) {
            is IOException -> "Network error: ${e.message}"
            is HttpException -> "Server error: ${e.message}"
            else -> defaultMessage
        }
        Log.e("SignUpViewModel", errorMessage.value, e)
    }

    // Public Functions
    fun senDOtp() {
        if (!validateFields()) return
        Log.e("check","inside senDOtp")
        viewModelScope.launch {
            isLoading.value = true
            try {
                sendOtp(email.value)
                if (isOtpSent.value) {
                    navigateToOtpScreen.value = true
                }
            }
            catch (e: Exception) {
                handleNetworkError(e, "OTP sending failed.")
            }
            finally {
                isLoading.value = false
            }
        }
    }
}
