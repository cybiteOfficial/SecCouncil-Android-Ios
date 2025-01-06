package com.example.seccouncil.network

import com.google.gson.annotations.SerializedName
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Response
import retrofit2.http.GET

// Define request data classes
data class SignUpRequest(
    val firstName: String,
    val lastName: String,
    val password: String,
    val confirmPassword: String,
    val email: String,
    val accountType: String,
    val otp: String
)

data class EmailRequest(val email: String)

// Define response data classes
data class OtpResponse(val success: Boolean, val message: String, val otp: String)
data class SignUpResponse(val success: Boolean, val user: User, val message: String)

data class User(
    val firstName: String,
    val lastName: String,
    val email: String,
    val accountType: String,
    val active: Boolean,
    val approved: Boolean,
    val additionalDetails: String,
    val courses: List<String>,
    val image: String,
    val courseProgress: List<String>,
    val _id: String,
    val createdAt: String,
    val updatedAt: String
)

data class LoginUser(
    val firstName: String,
    val lastName: String,
    val email: String,
    val accountType: String,
    val active: Boolean,
    val approved: Boolean,
    val additionalDetails: LoginAdditionalDetails,
    val courses: List<String>,
    val image: String,
    val courseProgress: List<String>,
    val _id: String,
    val createdAt: String,
    val updatedAt: String,
    val token: String? = null
)

data class LoginAdditionalDetails(
    val _id: String,
    val gender: String?,
    val dateOfBirth: String?,
    val about: String?,
    val contactNumber: String?,
    @SerializedName("__v") val v: Int
)

// Login Request
data class LoginRequest(
    val email: String,
    val password: String
)

// Login Response
data class LoginResponse(
    val success: Boolean,
    val token: String,
    val user: LoginUser,
    val message: String
)

interface ApiInterface {
    // Send OTP API
    @POST("sendotp")
    suspend fun sendOtp(@Body request: EmailRequest): Response<OtpResponse>

    // Sign-Up API
    @POST("signup")
    suspend fun signUp(@Body request: SignUpRequest): Response<SignUpResponse>

    // Login API
    @POST("login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

}