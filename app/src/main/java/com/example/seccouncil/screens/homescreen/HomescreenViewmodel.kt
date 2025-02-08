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
import com.example.seccouncil.network.getAllCourseDetailsModel.GetAllCourse
import com.example.seccouncil.network.getAllCourseDetailsModel.NetworkResponse
import com.example.seccouncil.network.getCourseDetail.GetCourseDetail
import com.example.seccouncil.network.getCourseDetail.GetCourseDetailRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
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
    }

    private val _getCourseDetailResult = MutableStateFlow<NetworkResponse<GetCourseDetail>?>(null)
    val getCourseDetailResult: StateFlow<NetworkResponse<GetCourseDetail>?> = _getCourseDetailResult

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
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
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

    // Handle retry logic explicitly
    fun retryGetCourses() {
        viewModelScope.launch {
            _getAllCourseResult.emit(NetworkResponse.Loading) // Reset to loading state before retrying
            getAllCourse() // Trigger the API call again
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