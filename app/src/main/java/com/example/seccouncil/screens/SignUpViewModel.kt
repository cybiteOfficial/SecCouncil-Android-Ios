package com.example.seccouncil.screens

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.seccouncil.model.UserDetails
import com.example.seccouncil.network.ApiService
import com.example.seccouncil.network.EmailRequest
import com.example.seccouncil.network.SignUpRequest
import com.example.seccouncil.utlis.DataStoreManger
import com.rejowan.ccpc.Country
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class SignUpViewModel(
    private val dataStoreManager: DataStoreManger, // Inject the DataStoreManager
    private val onRegisterSuccess: () -> Unit
) : ViewModel() {

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

    // Input Fields
    val email = mutableStateOf("")
    val password = mutableStateOf("")
    val confirmPassword = mutableStateOf("")
    val otpUser = mutableStateOf("")
    val firstname = mutableStateOf("")
    val lastname = mutableStateOf("")

    val countryCode = mutableStateOf(Country.India)
    val phoneNumber = mutableStateOf("")

    // Update Input Fields

    fun onCountryCodeChange(cC: Country) {
        countryCode.value = cC
    }

    fun onPhoneNumberChange(pPN: String) {
        phoneNumber.value = pPN
        Log.e("PhoneNumber", "${countryCode.value.countryCode}${phoneNumber.value}")
    }

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

        if (firstname.value.trim().isEmpty() || lastname.value.trim()
                .isEmpty() || email.value.trim().isEmpty() ||
            password.value.trim().isEmpty() || confirmPassword.value.trim().isEmpty()
        ) {
            errors.add("All fields are required.")
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.value.trim()).matches()) {
            errors.add("Invalid email address.")
        } else if (password.value != confirmPassword.value) {
            errors.add("Passwords do not match.")
        } else if (!Regex("^(?=.*[A-Z])(?=.*[!@#\$%^&*(),.?\":{}|<>])(?=.*\\d)[A-Za-z\\d!@#\$%^&*(),.?\":{}|<>]{8,}\$")
                .matches(password.value)
        ) {
            errors.add("Password must be at least 8 characters, contain an uppercase letter, special character, and number.")
        } else if (phoneNumber.value.isEmpty()) {
            errors.add("Phone number is required.")
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
                isLoading.value = false
            }
        } else {
            errorMessage.value = "Invalid OTP. Please check and try again."
        }
    }


    // Network Requests
    private suspend fun sendOtp(email: String) {
        try {
            Log.i(
                "Hero2",
                "${firstname.value}"
                        + "${lastname.value}" +
                        "${countryCode.value.countryCode}" +
                        "${confirmPassword.value}" +
                        "${phoneNumber.value}"+
                        "${email}"+
                        "${otpUser.value}"
            )
            val response = ApiService.api.sendOtp(EmailRequest(email))
            if (response.isSuccessful && response.body()?.success == true) {
                otpSentMessage.value = response.body()?.message.orEmpty()
                otp.value = response.body()?.otp.orEmpty()
                Log.e("check", "OTP send is ${otp}")
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
                contactNumber = "${countryCode.value.countryCode}${phoneNumber.value}",
                otp = otpUser.value
            )
            val response = ApiService.api.signUp(request)
            if (response.isSuccessful && response.body()?.success == true) {
                isSignUpSuccessful.value = true
                onRegisterSuccess()
                saveUserDetailsToDataStore(onRegisterSuccess)
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
            is IOException ->"Please check your internet connection and try again." //"Network error: ${e.message}"
            is HttpException -> "Server is currently unavailable. Please try later."//"Server error: ${e.message}"
            else -> defaultMessage
        }
        Log.e("SignUpViewModel", errorMessage.value, e)
    }

    fun saveUserDetailsToDataStore(onRegisterSuccess: () -> Unit) {
        if (isSignUpSuccessful.value) {
            val userDetails = UserDetails(
                emailAddress = email.value,
                name = "${firstname.value} ${lastname.value}",
                mobileNumber = "${countryCode.value.countryCode}${phoneNumber.value}"
            )
            viewModelScope.launch {
                try {
                    dataStoreManager.saveToDataStore(userDetails)
                    onRegisterSuccess()
                } catch (e: Exception) {
                    Log.e("SignUpViewModel", "Error saving user details: ${e.message}", e)
                }
            }
        }
    }
    // Public Functions
    fun senDOtp() {
        if (!validateFields()) return
        Log.e("check", "inside senDOtp")
        viewModelScope.launch {
            isLoading.value = true
            try {
                sendOtp(email.value)
                if (isOtpSent.value) {
                    navigateToOtpScreen.value = true
                    Log.i(
                        "SendOtpDebug",
                        "First Name: ${firstname.value}, Last Name: ${lastname.value}, " +
                                "Country Code: ${countryCode.value.countryCode}, Phone Number: ${phoneNumber.value}, " +
                                "Email: ${email}, OTP: ${otpUser.value}"
                    )
                }
            } catch (e: Exception) {
                handleNetworkError(e, "OTP sending failed.")
            } finally {
                isLoading.value = false
            }
        }
    }
}

// Define a factory class that creates ViewModels
// This class takes two parameters:
// 1. dataStoreManager: to handle data storage
// 2. onRegisterSuccess: a callback function that takes no parameters and returns nothing
class SignUpViewModelFactory(
    private val dataStoreManager: DataStoreManger,
    private val onRegisterSuccess: () -> Unit
) : ViewModelProvider.Factory {

    // Override the create method from ViewModelProvider.Factory
    // This method is responsible for creating ViewModel instances
    // The 'T' means it can return any type that extends ViewModel
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        // Check if the requested class (modelClass) is SignUpViewModel
        // or a parent class of SignUpViewModel
        if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
            // If it is, create a new instance of SignUpViewModel with our parameters
            // and cast it to type T
            return SignUpViewModel(dataStoreManager, onRegisterSuccess) as T
        }

        // If the requested class is not SignUpViewModel, throw an error
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


