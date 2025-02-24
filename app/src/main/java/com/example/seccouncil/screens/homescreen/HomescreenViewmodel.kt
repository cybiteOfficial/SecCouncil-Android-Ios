package com.example.seccouncil.screens.homescreen

import android.content.ContentResolver
import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.seccouncil.utlis.DataStoreManger
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.seccouncil.network.ApiService
import com.example.seccouncil.network.CourseRequest
import com.example.seccouncil.network.getAllCourseDetailsModel.GetAllCourse
import com.example.seccouncil.network.getAllCourseDetailsModel.NetworkResponse
import com.example.seccouncil.network.getCourseDetail.GetCourseDetail
import com.example.seccouncil.network.getCourseDetail.GetCourseDetailRequest
import com.example.seccouncil.network.getEnrolledCourse.EnrolledCourse
import com.example.seccouncil.network.getFullCourseDetails.SubSection
import com.example.seccouncil.network.getFullCourseDetails.getFullCourseDetailResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException
import java.util.UUID

//class HomescreenViewmodel {
//}

class HomescreenViewmodel(
    private val context: Context,
    private val dataStoreManager: DataStoreManger
) : ViewModel() {

    private val retrofitApi = ApiService.api
    private val _getAllCourseResult = MutableStateFlow<NetworkResponse<GetAllCourse>?>(null)
    val getAllCourseResult: StateFlow<NetworkResponse<GetAllCourse>?> = _getAllCourseResult

    private val _videoItems = MutableStateFlow<List<SubSection>>(emptyList())
    val videoItems: StateFlow<List<SubSection>> = _videoItems


    private val _enrolledCourses = MutableStateFlow<NetworkResponse<EnrolledCourse>?>(null)
    val enrolledCourses: StateFlow<NetworkResponse<EnrolledCourse>?> = _enrolledCourses


    // StateFlow that holds the latest image file name
    private val _imageFileName = MutableStateFlow<String?>(null)
    val imageFileName: StateFlow<String?> = _imageFileName

    init {
        // On ViewModel init, load the existing file name from DataStore
        viewModelScope.launch {
            dataStoreManager.getImageFileName().collect { savedFileName ->
                _imageFileName.value = savedFileName
            }
        }
        getAllCourse()
        getEnrolledCourse()
    }
    private val _isUploading = MutableStateFlow(false)
    val isUploading: StateFlow<Boolean> = _isUploading


    private val _getCourseDetailResult = MutableStateFlow<NetworkResponse<GetCourseDetail>?>(null)
    val getCourseDetailResult: StateFlow<NetworkResponse<GetCourseDetail>?> = _getCourseDetailResult

    private val _getFullCourseDetailResult = MutableStateFlow<NetworkResponse<getFullCourseDetailResponse>?>(null)
    val getFullCourseDetailResult: StateFlow<NetworkResponse<getFullCourseDetailResponse>?> = _getFullCourseDetailResult

    /**
     * Saves an image to the app's internal storage and updates DataStore.
     */
    fun saveImageToInternalStorage(uri: Uri) {
        viewModelScope.launch {
            val contentResolver: ContentResolver = context.contentResolver
            val fileExtension = contentResolver.getType(uri)?.split("/")?.lastOrNull() ?: "jpg"
            val uniqueName = "${UUID.randomUUID()}.$fileExtension"
            val file = File(context.filesDir, uniqueName)

            try {
                contentResolver.openInputStream(uri)?.use { input ->
                    file.outputStream().use { output -> input.copyTo(output) }
                }
                // Save the filename in DataStore
                dataStoreManager.saveImageFileName(uniqueName)
                // Also update the ViewModel's flow
                _imageFileName.value = uniqueName

                // Upload the image to the API after saving it locally
                uploadProfilePicture(context, uri)  // <-- NEWLY ADDED
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private val _tempImageUri = MutableStateFlow<Uri?>(null)
    val tempImageUri: StateFlow<Uri?> = _tempImageUri

    fun setTempImageUri(uri: Uri) {
        _tempImageUri.value = uri
    }

    private val _uploadStatus = MutableStateFlow<String?>(null)
    val uploadStatus: StateFlow<String?> = _uploadStatus

//    suspend fun uploadProfilePicture(context: Context, imageUri: Uri) {
//        val api = ApiService.createAuthenticatedApi(context)
//
//        val token = runBlocking {
//            DataStoreManger(context).getStoredToken().firstOrNull()
//        }
//
//        if (token.isNullOrEmpty()) {
//            Log.e("Profile Update", "Token is missing!")
//            return
//        }
//
//        // Convert URI to File
//        val contentResolver = context.contentResolver
//        val file = File(context.cacheDir, "profile_image.jpg")
//
//        contentResolver.openInputStream(imageUri)?.use { inputStream ->
//            file.outputStream().use { outputStream ->
//                inputStream.copyTo(outputStream)
//            }
//        }
//
//        val requestBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
//        val imagePart = MultipartBody.Part.createFormData("displayPicture", file.name, requestBody)
//
//        try {
//            _isUploading.value = true // Start loading
//            val response = api.updateDisplayPicture("Bearer $token", imagePart)
//            if (response.isSuccessful && response.body()?.success == true) {
//                Log.d("Profile Update", "Image updated successfully: ${response.body()?.data?.image}")
//                _uploadStatus.value = "Image updated successfully!"
//
//            } else {
//                val errorMsg = response.errorBody()?.string() ?: "Unknown error"
//                Log.e("Profile Update", "Failed to update image: ${response.errorBody()?.string()}")
//                _uploadStatus.value = "Failed to update: $errorMsg"
//            }
//        } catch (e: Exception) {
//            Log.e("Profile Update", "Error updating profile picture", e)
//            _uploadStatus.value = "Error updating image: ${e.localizedMessage}"
//        }
//    }

    suspend fun uploadProfilePicture(context: Context, imageUri: Uri) {
        val api = ApiService.createAuthenticatedApi(context)

        val token = DataStoreManger(context).getStoredToken().firstOrNull()

        if (token.isNullOrEmpty()) {
            Log.e("Profile Update", "Token is missing!")
            _uploadStatus.value = "Failed to update: Token missing"
            return
        }

        val contentResolver = context.contentResolver
        val file = File(context.cacheDir, "profile_image.jpg")

        contentResolver.openInputStream(imageUri)?.use { inputStream ->
            file.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }

        val requestBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imagePart = MultipartBody.Part.createFormData("displayPicture", file.name, requestBody)

        try {
            _isUploading.value = true // Start loading

            val response = api.updateDisplayPicture("Bearer $token", imagePart)

            if (response.isSuccessful && response.body()?.success == true) {
                Log.d("Profile Update", "Image updated successfully: ${response.body()?.data?.image}")
                _uploadStatus.value = "Image updated successfully!"
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Unknown error"
                Log.e("Profile Update", "Failed to update image: $errorMsg")
                _uploadStatus.value = "Failed to update: $errorMsg"
            }
        } catch (e: Exception) {
            Log.e("Profile Update", "Error updating profile picture", e)
            _uploadStatus.value = "Error updating image: ${e.localizedMessage}"
        } finally {
            _isUploading.value = false // Stop loading
        }
    }
    fun clearUploadStatus() {
        _uploadStatus.value = null
    }

    /**
     * Removes the image filename from DataStore.
     */
    fun removeImageFileName() {
        viewModelScope.launch {
            dataStoreManager.removeImageFileName()
        }
    }


    fun getAllCourse() {
        viewModelScope.launch {
            _getAllCourseResult.emit(NetworkResponse.Loading) // Ensure state is set to loading before retrying

            try {
                Log.e("Ch01","$getAllCourseResult $_getAllCourseResult")
                val response = retrofitApi.getAllCourses()
                if (response.isSuccessful) {
                    response.body()?.let {
                        _getAllCourseResult.emit(NetworkResponse.Success(it)) // Update state to Success
                    } ?: _getAllCourseResult.emit(NetworkResponse.Error("Empty response"))
                } else {
                    _getAllCourseResult.emit(NetworkResponse.Error("HTTP Error: ${response.code()}"))
                }
            } catch (e: Exception) {
                _getAllCourseResult.emit(NetworkResponse.Error("Network error"))
            }
        }
    }

    fun getEnrolledCourse() {
        val apiService = ApiService.createAuthenticatedApi(context)

        viewModelScope.launch {
            try {
                val token = DataStoreManger(context).getStoredToken().firstOrNull()
                if (token.isNullOrEmpty()) {
                    return@launch
                }

                val response = apiService.getEnrolledCourse(authHeader = "Bearer $token")

                if (response.isSuccessful) {
                    response.body()?.let {
                        _enrolledCourses.emit(NetworkResponse.Success(it))
                    }?:_enrolledCourses.emit(NetworkResponse.Error("Emtpy response"))
                } else {
                    _enrolledCourses.emit(NetworkResponse.Error("HTTP Error: ${response.code()}"))
                }
            } catch (e: Exception) {
                _enrolledCourses.emit(NetworkResponse.Error("Network error"))
            }
        }
    }


    // Handle retry logic explicitly
    fun retryGetCourses() {
        viewModelScope.launch {
            _getAllCourseResult.emit(NetworkResponse.Loading) // Reset to loading state before retrying
            getAllCourse() // Trigger the API call again
            _enrolledCourses.emit(NetworkResponse.Loading)
            getEnrolledCourse()
        }
    }

    fun getCourseDetail(courseId: String) {
        viewModelScope.launch {
            Log.d("CourseDetail", "Fetching details for courseId: $courseId") // Debug log
            _getCourseDetailResult.emit(NetworkResponse.Loading)

            try {
                val response = retrofitApi.getCourseDetail(GetCourseDetailRequest(courseId))
                if (response.isSuccessful) {
                    response.body()?.let {
                        _getCourseDetailResult.emit(NetworkResponse.Success(it))
                    } ?: _getCourseDetailResult.emit(NetworkResponse.Error("Empty response"))
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("CourseDetail", "Error ${response.code()}: $errorMessage") // Log full error
                    _getCourseDetailResult.emit(NetworkResponse.Error("HTTP Error: ${response.code()} - $errorMessage"))
                }
            } catch (e: Exception) {
                _getCourseDetailResult.emit(NetworkResponse.Error("Network error: ${e.message}"))
            }
        }
    }

    fun getFullCourseDetail(courseId: String) {
        val api = ApiService.createAuthenticatedApi(context)
        viewModelScope.launch {
            Log.d("FullCourseDetail", "Fetching full details for courseId: $courseId")
            _getFullCourseDetailResult.emit(NetworkResponse.Loading)

            try {
                val token = DataStoreManger(context).getStoredToken().firstOrNull()
                if (token.isNullOrEmpty()) {
                    Log.e("FullCourseDetail", "Token not found")
                    _getFullCourseDetailResult.emit(NetworkResponse.Error("Authorization token missing"))
                    return@launch
                }

                val request = CourseRequest(courseId) // Use data class
                val response = api.getFullCourseDetail("Bearer $token", request)

                if (response.isSuccessful) {
                    response.body()?.let {result ->
                        _getFullCourseDetailResult.emit(NetworkResponse.Success(result))
                        val subSections = result.data.courseDetails.courseContent
                            .flatMap { it.subSection } // Flatten the list of subsections

                        _videoItems.value = subSections // Emit the flattened list of subsections
                        Log.d("FullCourseDetail", "Video items fetched: $subSections")
                        Log.d("FullCourseDetail", "Video items fetched: ${subSections.size}")
                        Log.d("FullCourseDetail", "Video items fetched: ${videoItems.value}")
                    } ?: _getFullCourseDetailResult.emit(NetworkResponse.Error("Empty response"))
                    Log.e("FullCourseDetail","--> $_videoItems")
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("FullCourseDetail", "Error ${response.code()}: $errorMessage")
                    _getFullCourseDetailResult.emit(NetworkResponse.Error("HTTP Error: ${response.code()} - $errorMessage"))
                }
            } catch (e: Exception) {
                Log.e("FullCourseDetail", "Network error: ${e.message}")
                _getFullCourseDetailResult.emit(NetworkResponse.Error("Network error: ${e.message}"))
            }
        }
    }
}




/**
 * A custom factory class to create PickImageViewModel instances,
 * passing in the necessary Context and DataStoreManager.
 */
class MyViewModelFactory(
    private val context: Context,
    private val dataStoreManager: DataStoreManger
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Check if the requested ViewModel class is PickImageViewModel
        if (modelClass.isAssignableFrom(HomescreenViewmodel::class.java)) {
            return HomescreenViewmodel(
                context = context,
                dataStoreManager = dataStoreManager
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}