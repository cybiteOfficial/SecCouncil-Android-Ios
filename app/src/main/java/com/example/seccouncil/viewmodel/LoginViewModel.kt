package com.example.seccouncil.viewmodel

//import com.example.seccouncil.SecCouncilReleaseApplication
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seccouncil.network.ApiService
import com.example.seccouncil.network.LoginRequest
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class LoginViewModel() : ViewModel() {

    // UI States
    val errorMessage = mutableStateOf("")
    val isLoading = mutableStateOf(false)
    val isLoginSuccessful = mutableStateOf(false)
    val navigateToHomeScreen = mutableStateOf(false)
    val loginMessage = mutableStateOf("")

    // Input Fields
    val loginEmail = mutableStateOf("")
    val loginPassword = mutableStateOf("")

    // Update Input Fields
    fun onLoginEmailChange(newEmail: String) {
        loginEmail.value = newEmail
    }

    fun onLoginPasswordChange(newPassword: String) {
        loginPassword.value = newPassword
    }


    // Validation
    private fun validateLoginFields(): Boolean {
        val errors = mutableListOf<String>()

        if (loginEmail.value.trim().isEmpty() || loginPassword.value.trim().isEmpty()) {
            errors.add("Email and password are required.")
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(loginEmail.value.trim()).matches()) {
            errors.add("Invalid email address.")
        }

        return if (errors.isNotEmpty()) {
            errorMessage.value = errors.joinToString(separator = "\n")
            false
        } else {
            errorMessage.value = ""
            true
        }
    }

    // Network Request
    fun loginUser() {
        if (!validateLoginFields()) return

        viewModelScope.launch {
            isLoading.value = true
            try {
                val request = LoginRequest(
                    email = loginEmail.value,
                    password = loginPassword.value
                )
                val response = ApiService.api.login(request)
                if (response.isSuccessful && response.body()?.success == true) {
                    isLoginSuccessful.value = true
                    loginMessage.value = response.body()?.message.orEmpty() // Save login state
                    navigateToHomeScreen.value = true
                    Log.e("check", "login success")
                } else {
                    errorMessage.value = response.errorBody()?.string().orEmpty()
                    isLoginSuccessful.value = false
                    Log.e("check", "login failed")
                }
            } catch (e: Exception) {
                handleNetworkError(e, "Login failed.")
                isLoginSuccessful.value = false
            } finally {
                isLoading.value = false
            }
        }
    }

    private fun handleNetworkError(e: Exception, defaultMessage: String) {
        errorMessage.value = when (e) {
            is IOException -> "Network error: ${e.message}"
            is HttpException -> "Server error: ${e.message}"
            else -> defaultMessage
        }
        Log.e("LoginViewModel", errorMessage.value, e)
    }
}