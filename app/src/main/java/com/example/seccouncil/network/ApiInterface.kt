package com.example.seccouncil.network

import com.example.seccouncil.network.ResetPassword.ResetPasswordRequest
import com.example.seccouncil.network.ResetPassword.ResetPasswordResponse
import com.example.seccouncil.network.capturePayment.capturePaymentResponseX
import com.example.seccouncil.network.getAllCourseDetailsModel.GetAllCourse
import com.example.seccouncil.network.getAverageRating.getAverageRatingRequest
import com.example.seccouncil.network.getAverageRating.getAverageRatingResponse
import com.example.seccouncil.network.getCourseDetail.GetCourseDetail
import com.example.seccouncil.network.getCourseDetail.GetCourseDetailRequest
import com.example.seccouncil.network.getEnrolledCourse.EnrolledCourse
import com.example.seccouncil.network.getFullCourseDetails.getFullCourseDetailResponse
import com.example.seccouncil.network.getUserDetails.UserDetailsResponse
import com.example.seccouncil.network.updateProfile.updateProfileRequest
import com.example.seccouncil.network.updateProfile.updateProfileResponse
import com.example.seccouncil.network.updateProfilePicture.UpdateProfilePictureResponse
import com.example.seccouncil.network.verifyPaymentRequest.verifyPaymentRequest
import com.google.gson.annotations.SerializedName
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part

// Define request data classes
data class SignUpRequest(
    val firstName: String,
    val lastName: String,
    val password: String,
    val confirmPassword: String,
    val email: String,
    val accountType: String,
    val otp: String,
    val contactNumber: String
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

data class CapturePaymentRequest(
    val courses: List<String>
)

data class verifyPaymentResponse(
    val success: Boolean,
    val message: String
)

data class CourseRequest(
    val courseId: String
)
data class ErrorResponse(
    val success: Boolean,
    val message: String
)


interface ApiInterface {
    // Send OTP API
    @POST("/api/v1/auth/sendotp")
    suspend fun sendOtp(@Body request: EmailRequest): Response<OtpResponse>

    // Sign-Up API
    @POST("/api/v1/auth/signup")
    suspend fun signUp(@Body request: SignUpRequest): Response<SignUpResponse>

    // Login API
    @POST("/api/v1/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @GET("/api/v1/course/getAllCourses")
    suspend fun getAllCourses(): Response<GetAllCourse>

    @POST("/api/v1/course/getCourseDetails")
    suspend fun getCourseDetail(@Body request: GetCourseDetailRequest): Response<GetCourseDetail>

    @GET("/api/v1/profile/getUserDetails")
    suspend fun getUserDetails(@Header("Authorization") authHeader: String): Response<UserDetailsResponse>

    @Multipart
    @PUT("/api/v1/profile/updateDisplayPicture")
    suspend fun updateDisplayPicture(
        @Header("Authorization") authHeader: String,
        @Part displayPicture: MultipartBody.Part
    ): Response<UpdateProfilePictureResponse>

    @POST("/api/v1/payment/capturePayment")
    suspend fun capturePayment(
        @Header("Authorization") authHeader: String,
        @Body request: CapturePaymentRequest
    ): Response<capturePaymentResponseX>

    @GET("/api/v1/profile/getEnrolledCourses")
    suspend fun getEnrolledCourse(
        @Header("Authorization") authHeader: String
    ):Response<EnrolledCourse>


    // Define a POST request to the endpoint "/api/v1/payment/verifyPayment"
    @POST("/api/v1/payment/verifyPayment")
    // This function is a suspend function, which means it can be paused and resumed at a later time
    suspend fun verifyPayment(
        // The Authorization header is passed to the request for authentication
        @Header("Authorization") authHeader: String,
        // The body of the request, which contains the payment verification details
        @Body request: verifyPaymentRequest
    ): Response<verifyPaymentResponse> // The function returns a Response object containing verifyPaymentResponse

    @POST("/api/v1/course/getFullCourseDetails")
    suspend fun getFullCourseDetail(
        @Header("Authorization") authHeader: String,
        @Body request : CourseRequest
    ):Response<getFullCourseDetailResponse>

    @POST("/api/v1/auth/reset-password-token")
    suspend fun resetPassword(
        @Body request: ResetPasswordRequest
    ): Response<ResetPasswordResponse>

    @PUT("/api/v1/profile/updateProfile")
    suspend fun updateProfile(
        @Header("Authorization") authHeader: String,
        @Body request: updateProfileRequest
    ): Response<updateProfileResponse>

    @HTTP(method = "GET", path = "/api/v1/course/getAverageRating", hasBody = true)
    suspend fun getAverageRatingWithBody(
        @Body request: getAverageRatingRequest
    ): Response<getAverageRatingResponse>
}