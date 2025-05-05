package com.example.seccouncil.viewmodel

//import com.example.seccouncil.SecCouncilReleaseApplication
import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.seccouncil.model.UserDetails
import com.example.seccouncil.model.UserProfilePhoto
import com.example.seccouncil.network.ApiService
import com.example.seccouncil.network.ErrorResponse
import com.example.seccouncil.network.LoginRequest
import com.example.seccouncil.network.getUserDetails.UserDetailsResponse
import com.example.seccouncil.utlis.DataStoreManger
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class LoginViewModel(
    private val context: Context
) : ViewModel() {

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
    private val _userDetails = MutableLiveData<UserDetailsResponse?>()
    val userDetails: LiveData<UserDetailsResponse?> = _userDetails

    fun loadUserDetails() {
        viewModelScope.launch {
            _userDetails.value = fetchUserDetails(context)
        }
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
                    email = loginEmail.value.trim(),
                    password = loginPassword.value.trim()
                )
                Log.e("LoginRequestCheck", "Email: ${loginEmail.value}, Password: ${loginPassword.value}")
                // Call API
                val response = ApiService.api.login(request)
                Log.e("ApiResponse", "${response.isSuccessful} Code: ${response.code()} Body: ${response.body()?.success} ErrorBody: ${response.errorBody()?.string()}")
//                if (response.isSuccessful && response.body()?.success == true) {
//                    val token = response.body()?.token.orEmpty()
//                    Log.e("Tokenn", "$token")
//                    if (token.isNotEmpty()) {
//                        // Store JWT token
//                        DataStoreManger(context).saveJwtToken(token)
//
//                        // Fetch user details
//                        val _userDetails = fetchUserDetails(context)
//                        _userDetails?.let {
//                            DataStoreManger(context).saveToDataStore(
//                                UserDetails(
//                                    emailAddress = it.data.email,
//                                    name = "${it.data.firstName} ${it.data.lastName}",
//                                    mobileNumber = it.data.contactNumber
//                                )
//                            )
//                            DataStoreManger(context).saveUserProfileImage(
//                                UserProfilePhoto(
//                                    photo = _userDetails.data.image
//                                )
//                            )
//                        }
//                    }
//                    isLoginSuccessful.value = true
//                    loginMessage.value = response.body()?.message.orEmpty()
//                    navigateToHomeScreen.value = true
//                    Log.e("check", "login success")
//                }
                if (response.isSuccessful && response.body()?.success == true) {
                    val token = response.body()?.token.orEmpty()
                    Log.e("Tokenn", "$token")
                    if (token.isNotEmpty()) {
                        // Store JWT token
                        DataStoreManger(context).saveJwtToken(token)

                        // Store user details directly from login response
                        response.body()?.user?.let { user ->
                            DataStoreManger(context).saveToDataStore(
                                UserDetails(
                                    emailAddress = user.email,
                                    name = "${user.firstName} ${user.lastName}",
                                    mobileNumber = user.additionalDetails.contactNumber.orEmpty(),
                                    gender = user.additionalDetails.gender.orEmpty(),
                                    about = user.additionalDetails.about.orEmpty(),
                                    dob = user.additionalDetails.dateOfBirth.orEmpty()
                                )
                            )
                            DataStoreManger(context).saveUserProfileImage(
                                UserProfilePhoto(
                                    photo = user.image
                                )
                            )
                        }
                    }
                    isLoginSuccessful.value = true
                    loginMessage.value = response.body()?.message.orEmpty()
                    navigateToHomeScreen.value = true
                    Log.e("check", "login success")
                }
                else {
                    // Only now read errorBody safely
                    val errorBodyString = response.errorBody()?.string().orEmpty()
                    val errorMessageFromResponse = try {
                        val gson = Gson()
                        val errorResponse = gson.fromJson(errorBodyString, ErrorResponse::class.java)
                        errorResponse.message
                    } catch (e: Exception) {
                        "Login failed"
                    }
                    errorMessage.value = errorMessageFromResponse
                    isLoginSuccessful.value = false
                }
            }catch (e: HttpException) {
                handleNetworkError(e, "Login failed.")
                isLoginSuccessful.value = false
            } catch (e: IOException) {
                handleNetworkError(e, "No internet connection.")
                isLoginSuccessful.value = false
            } catch (e: Exception) {
                handleNetworkError(e, "Unexpected error occurred.")
                isLoginSuccessful.value = false
            }
            finally {
                isLoading.value = false
            }
        }
    }

    suspend fun fetchUserDetails(context: Context): UserDetailsResponse? {
        val api = ApiService.createAuthenticatedApi(context) // Create authenticated API instance
        val token = DataStoreManger(context).getStoredToken().first() // Get JWT token

        return if (!token.isNullOrEmpty()) {
            val response = api.getUserDetails("Bearer $token")
            if (response.isSuccessful) {
                response.body() // Return user details if successful
            } else {
                null // Handle failure case
            }
        } else {
            null // Token not available
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

class LoginViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            LoginViewModel(context) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}