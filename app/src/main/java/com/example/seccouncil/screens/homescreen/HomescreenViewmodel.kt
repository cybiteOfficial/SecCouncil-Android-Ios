package com.example.seccouncil.screens.homescreen

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.seccouncil.model.UserDetails
import com.example.seccouncil.network.ApiService
import com.example.seccouncil.network.CourseRequest
import com.example.seccouncil.network.getAllCourseDetailsModel.GetAllCourse
import com.example.seccouncil.network.getAllCourseDetailsModel.NetworkResponse
import com.example.seccouncil.network.getAverageRating.getAverageRatingRequest
import com.example.seccouncil.network.getCourseDetail.GetCourseDetail
import com.example.seccouncil.network.getCourseDetail.GetCourseDetailRequest
import com.example.seccouncil.network.getEnrolledCourse.EnrolledCourse
import com.example.seccouncil.network.getFullCourseDetails.SubSection
import com.example.seccouncil.network.getFullCourseDetails.getFullCourseDetailResponse
import com.example.seccouncil.network.updateProfile.updateProfileRequest
import com.example.seccouncil.utlis.DataStoreManger
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
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
    val api = ApiService.createAuthenticatedApi(context)
    private val _getAllCourseResult = MutableStateFlow<NetworkResponse<GetAllCourse>?>(null)
    val getAllCourseResult: StateFlow<NetworkResponse<GetAllCourse>?> = _getAllCourseResult

    private val _videoItems = MutableStateFlow<List<SubSection>>(emptyList())
    val videoItems: StateFlow<List<SubSection>> = _videoItems


    private val _enrolledCourses = MutableStateFlow<NetworkResponse<EnrolledCourse>?>(null)
    val enrolledCourses: StateFlow<NetworkResponse<EnrolledCourse>?> = _enrolledCourses


    // StateFlow that holds the latest image file name
    private val _imageFileName = MutableStateFlow<String?>(null)
    val imageFileName: StateFlow<String?> = _imageFileName

    fun setTempImageUri(uri: Uri?) {
        _tempImageUri.value = uri
    }

    init {
        // On ViewModel init, load the existing file name from DataStore
// On ViewModel init, launch a coroutine in the viewModelScope
        viewModelScope.launch {
            // Retrieve the existing file name from DataStore and collect the value
            dataStoreManager.getImageFileName().collect { savedFileName ->
                // Update the StateFlow _imageFileName with the retrieved value
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
            // Get the content resolver from the context
            val contentResolver: ContentResolver = context.contentResolver

            // Determine the file extension from the URI's MIME type, defaulting to "jpg" if not found
            val fileExtension = contentResolver.getType(uri)?.split("/")?.lastOrNull() ?: "jpg"

            // Generate a unique file name using a UUID and the determined file extension
            val uniqueName = "${UUID.randomUUID()}.$fileExtension"

            // Create a new File object in the app's files directory with the unique file name
            val file = File(context.filesDir, uniqueName)
            try {
                // Open an InputStream to read data from the given URI and use it
                contentResolver.openInputStream(uri)?.use { input ->
                    // Open an OutputStream to write data to the created file and use it
                    file.outputStream().use { output ->
                        // Copy the data from the InputStream to the OutputStream
                        input.copyTo(output)
                    }
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

//    fun setTempImageUri(uri: Uri) {
//        _tempImageUri.value = uri
//    }

    private val _uploadStatus = MutableStateFlow<String?>(null)
    val uploadStatus: StateFlow<String?> = _uploadStatus

    fun updateProfile(
        about: String,
        contactNumber: Long,
        dateOfBirth: String,
        firstName: String,
        gender: String,
        lastName: String
    ){
        viewModelScope.launch {
            _uploadStatus.value = "Updating Profile..."
            try{
                val token = dataStoreManager.getStoredToken().firstOrNull()
                if(token.isNullOrBlank()){
                    _uploadStatus.value = "Unauthorized: Token not found"
                    return@launch
                }
                _isUploading.value = true
                val request = updateProfileRequest(
                    about = about,
                    contactNumber = contactNumber,
                    dateOfBirth = dateOfBirth,
                    firstName = firstName,
                    gender = gender,
                    lastName = lastName
                )

                val response = api.updateProfile(
                    authHeader = "Bearer $token",
                    request = request
                )
                _uploadStatus.value = if (response.isSuccessful && response.body()?.success == true) {
                    "Profile updated successfully"
                } else {
                    "Failed to update profile: ${response.message()}"
                }
                if(_uploadStatus.value=="Profile updated successfully"){
                    viewModelScope.launch {
                        dataStoreManager.saveAdditionalDetails(
                            about = about,
                            firstName = firstName,
                            lastName = lastName,
                            dob = dateOfBirth,
                            gender = gender,
                            phoneNumber = contactNumber.toString()
                        )
                    }
                }

            }catch (e: Exception){
                    e.printStackTrace()
                _uploadStatus.value = "Error occurred: ${e.localizedMessage ?: "Unknown Error"}"
            }
            finally {
                _isUploading.value = false
            }
        }
    }

    // Function to upload the user's profile picture to the server
    suspend fun uploadProfilePicture(context: Context, imageUri: Uri) {
        // Create an instance of the API service with authentication

        // Get the stored authentication token from the DataStore
        val token = DataStoreManger(context).getStoredToken().firstOrNull()
        /*

        .firstOrNull() is a Kotlin extension function that returns the first element of a collection or sequence — but with a safety feature!
         If the collection is empty, instead of throwing an exception, it returns null.

         */

        // Check if the token is missing
        if (token.isNullOrEmpty()) {
            Log.e("Profile Update", "Token is missing!")
            _uploadStatus.value = "Failed to update: Token missing"
            return // Stop the function if no token is found
        }

        // Create a temporary file to store the image in the cache directory
        val contentResolver = context.contentResolver
        val file = File(context.cacheDir, "profile_image.jpg")
        /*
        * "profile_image.jpg": The name of the file that will be created.
        * */

        // Copy the image data from the URI to the temporary file
        contentResolver.openInputStream(imageUri)?.use { inputStream ->
            file.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }

        // Prepare the image file to be sent in the API request
        val requestBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imagePart = MultipartBody.Part.createFormData("displayPicture", file.name, requestBody)

        try {
            _isUploading.value = true // Show loading state when the upload starts

            // Make the API call to upload the image
            val response = api.updateDisplayPicture("Bearer $token", imagePart)

            // Check if the response is successful
            if (response.isSuccessful && response.body()?.success == true) {
                Log.d("Profile Update", "Image updated successfully: ${response.body()?.data?.image}")
//                _uploadStatus.value = "Image updated successfully!"
            } else {
                // Handle API errors and log the error message
                val errorMsg = response.errorBody()?.string() ?: "Unknown error"
                Log.e("Profile Update", "Failed to update image: $errorMsg")
                _uploadStatus.value = "Failed to update: $errorMsg"
            }
        } catch (e: Exception) {
            // Catch and handle any unexpected errors
            Log.e("Profile Update", "Error updating profile picture", e)
//            _uploadStatus.value = "Error updating image: ${e.localizedMessage}"
        } finally {
            _isUploading.value = false // Stop loading state after the upload attempt finishes
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
    fun updateUserProfile(
        firstName: String,
        lastName: String,
        contactNumber: Long,
        about: String,
        dob: String,
        gender: String,
        imageUri: Uri?
    ) {
        viewModelScope.launch {
            val isProfileFilled = firstName.isNotBlank() &&
                    lastName.isNotBlank() &&
                    contactNumber != 0L &&
                    about.isNotBlank() &&
                    dob.isNotBlank() &&
                    gender.isNotBlank()

            val imageUploaded = imageUri != null

            // Handle cases
            when {
                imageUploaded && !isProfileFilled -> {
                    saveImageToInternalStorage(imageUri)
                    _uploadStatus.value = "Image uploaded successfully."
                }

                !imageUploaded && isProfileFilled -> {
                    updateProfile(
                        about = about,
                        contactNumber = contactNumber,
                        dateOfBirth = dob,
                        firstName = firstName,
                        gender = gender,
                        lastName = lastName
                    )
                    // Status already handled inside updateProfile
                }

                imageUploaded && isProfileFilled -> {
                    // Upload image first then update profile
                    saveImageToInternalStorage(imageUri)
                    // Wait a bit to avoid overlap, optional
                    delay(500)
                    updateProfile(
                        about = about,
                        contactNumber = contactNumber,
                        dateOfBirth = dob,
                        firstName = firstName,
                        gender = gender,
                        lastName = lastName
                    )
                    // Status handled inside updateProfile or fallback here
                    if (_uploadStatus.value?.contains("success", ignoreCase = true) == true) {
                        _uploadStatus.value = "Profile and image updated successfully."
                    }
                }
                else -> {
                    _uploadStatus.value = "Please fill profile or select image to update."
                }
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
//    var avgRating = mutableIntStateOf(0)
//    fun getAverageRating(courseId: String):Int {
//        viewModelScope.launch {
//            val response = ApiService.api.getAverageRatingWithBody(
//                getAverageRatingRequest(
//                    courseId = courseId
//                )
//            )
//            if (response.isSuccessful) {
//                response.body()?.let {
//                    avgRating.value = it.averageRating
//                }
//            } else {
//                avgRating.value = 0
//                Log.e("RatingError", response.message())
//            }
//        }
//        return avgRating.value
//    }
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


/*

What is Multipart?
	•	Multipart is a way to send multiple pieces of data in a single HTTP request.
	•	It’s commonly used when uploading files, images, or large data along with regular form fields.


What is MIME?
MIME stands for Multipurpose Internet Mail Extensions.

It tells the server what type of content you’re sending, so it knows how to handle it.

🔑 Example MIME types:
	•	Images: image/jpeg, image/png
	•	Text files: text/plain
	•	JSON data: application/json
	•	Videos: video/mp4


[   file.asRequestBody("image/jpeg".toMediaTypeOrNull())  ]
This tells the server:
➡️ “Hey, I’m sending a JPEG image!”

This tells the server:
➡️ “Hey, I’m sending a JPEG image!”

🛠️ Key Points:
	•	emit: Sends a value into the flow.
	•	collect: Receives and processes the emitted values.
	•	Flow is asynchronous: Values are emitted one at a time, and collectors react to each value.

🧠 Analogy:

Think of a flow as a water pipe — emit is like pouring water into the pipe, and collect is like putting your hand at the end to catch the water. 🚰

*/






/*

📘 flatMap Summary:
	•	Purpose: Transforms elements and flattens nested collections into a single list.
	•	Works on: Lists, Sequences, Flows, etc.
	•	Key difference:
	•	map: Transforms elements, returns a list of results.
	•	flatMap: Transforms elements and flattens nested results.

🟢 Syntax:

val result = collection.flatMap { element ->
    transform(element)
}

🔧 Example:

val nums = listOf(1, 2, 3)
val result = nums.flatMap { listOf(it, it * 10) }
println(result)

Output:

[1, 10, 2, 20, 3, 30]

🛠️ Use Cases:
	•	Flatten nested lists: Combine multiple lists into one.
	•	Split strings: Break words into characters.
	•	Transform API responses: Handle nested data, like users with multiple posts.


📘 .map Summary:
	•	Purpose: Transforms each element of a collection and returns a new list with transformed elements.
	•	Works on: Lists, Sequences, Flows, etc.
	•	Transformation: Applies a lambda function to each element.

🟢 Syntax:

val result = collection.map { element ->
    transform(element)
}

🔧 Example:

val nums = listOf(1, 2, 3)
val result = nums.map { it * 2 }
println(result)

Output:

[2, 4, 6]

🛠️ Use Cases:
	•	Transform data: Convert numbers, format strings, or change object properties.
	•	Create UI models: Map API responses to UI-friendly models.
	•	Data cleanup: Trim strings, normalize values, etc.

🔑 Key Difference (vs flatMap):
	•	map: Returns a list of transformed elements.
	•	flatMap: Returns a single flattened list if transformation returns collections.

 */