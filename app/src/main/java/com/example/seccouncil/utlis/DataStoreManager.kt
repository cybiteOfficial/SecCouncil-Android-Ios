package com.example.seccouncil.utlis


import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.seccouncil.common.ProfileImage
import com.example.seccouncil.model.UserDetails
import com.example.seccouncil.model.UserProfilePhoto
import com.example.seccouncil.network.ApiService
import com.example.seccouncil.network.ApiService.api
import com.example.seccouncil.network.LoginRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map


// Define a constant for the name of our DataStore
const val USER_DATASTORE = "user_data"

// Create an extension property on Context class to easily access DataStore
// This creates a single instance of DataStore using the delegate pattern (by keyword)
val Context.preferenceDataStore : DataStore<Preferences> by preferencesDataStore(name = USER_DATASTORE)

// Define a class to manage all DataStore operations
class DataStoreManger(val context: Context) {

    // Companion object holds static members that can be accessed without class instance
    // this means we can access directly by using DataStoreManger.EMAIL no need to create val dataStore
    // = DataStoreManger(LocalContext.current)
    companion object {
        // Define keys for different preferences we want to store
        // These keys are used to read and write values in DataStore
        val EMAIL = stringPreferencesKey("EMAIL")
        val PASSWORD = stringPreferencesKey("PASSWORD")
        val MOBILE_NUMBER = stringPreferencesKey("PHONE")
        val NAME = stringPreferencesKey("NAME")
        val DOB = stringPreferencesKey("DOB")
        val GENDER = stringPreferencesKey("GENDER")
        val ABOUT = stringPreferencesKey("ABOUT")
        val IMAGE_FILE_NAME = stringPreferencesKey("image_file_name")
        private val PURCHASED_COURSES_KEY = stringSetPreferencesKey("purchased_courses")
        private val JWT_TOKEN_KEY = stringPreferencesKey("jwt_token")
        val PROFILE_IMAGE = stringPreferencesKey("profile_image")
    }

    // Suspend function to save user details to DataStore
    // Suspend keyword means this function can be paused and resumed (used for asynchronous operations)
    suspend fun saveToDataStore(userDetails: UserDetails) {
        // Access DataStore through context and edit its values
        context.preferenceDataStore.edit {
            // 'it' refers to the Preferences object we're editing
            // Store each user detail using its corresponding key
            it[EMAIL] = userDetails.emailAddress
            it[PASSWORD] = userDetails.password
            it[MOBILE_NUMBER] = userDetails.mobileNumber
            it[NAME] = userDetails.name
            it[DOB] = userDetails.dob
            it[GENDER]  = userDetails.gender
            it[ABOUT] = userDetails.about
        }
    }
    suspend fun saveAdditionalDetails(
        dob: String,
        firstName: String,
        lastName: String,
        phoneNumber: String,
        gender: String,
        about: String
    ){
        context.preferenceDataStore.edit {
            it[DOB] = dob
            it[MOBILE_NUMBER] = phoneNumber
            it[NAME] = "$firstName $lastName"
            it[GENDER] = gender
            it[ABOUT] = about
        }
    }
    suspend fun saveUserProfileImage(userProfile: UserProfilePhoto){

        context.preferenceDataStore.edit {
            it[PROFILE_IMAGE] = userProfile.photo
            Log.d("ProfileImage_","${it[PROFILE_IMAGE]} ... ")
        }

    }
    fun getFromSavedProfileImage()=context.preferenceDataStore.data.map {
        UserProfilePhoto(
            photo = it[PROFILE_IMAGE]?:""
        )
    }

    // Function to retrieve user details from DataStore
    // Returns a Flow (reactive stream) of UserDetails
    fun getFromDataStore() = context.preferenceDataStore.data.map {
        // Transform the preferences data into a UserDetails object
        // The ?: operator provides a default empty string if the value is null
        UserDetails(
            emailAddress = it[EMAIL] ?: "",
            name = it[NAME] ?: "",
            mobileNumber = it[MOBILE_NUMBER] ?: "",
            dob =  it[DOB]?:"",
            about = it[ABOUT]?:"",
            gender = it[GENDER]?:""
        )
    }

    // Suspend function to clear all data from DataStore
    suspend fun clearDataStore() = context.preferenceDataStore.edit {
        // Clear all stored preferences
        it.clear()
    }


    // This function saves the image filename to DataStore.
    // `suspend` means this function can be paused and resumed, allowing it to perform long-running operations without blocking the main thread.
    suspend fun saveImageFileName(fileName: String) {
        // `context.dataStore.edit` is used to modify the DataStore.
        context.preferenceDataStore.edit { preferences ->
            // `preferences[IMAGE_FILE_NAME] = fileName` stores the filename under the specified key.
            preferences[IMAGE_FILE_NAME] = fileName
        }
    }

    // This function retrieves the image filename from DataStore as a Flow.
    // `Flow<String?>` means it returns a stream of String values that can be null.
    fun getImageFileName(): Flow<String?> {
        // `context.dataStore.data` returns a Flow of the current Preferences.
        return context.preferenceDataStore.data.map { preferences ->
            // `preferences[IMAGE_FILE_NAME]` retrieves the value associated with the key.
            preferences[IMAGE_FILE_NAME]
        }
    }
    // This function removes the image filename from DataStore.
    // `suspend` means this function can be paused and resumed.
    suspend fun removeImageFileName() {
        // `context.dataStore.edit` is used to modify the DataStore.
        context.preferenceDataStore.edit { preferences ->
            // `preferences.remove(IMAGE_FILE_NAME)` removes the value associated with the key.
            preferences.remove(IMAGE_FILE_NAME)
        }
    }


    // Save Purchased Course ID
    suspend fun savePurchasedCourse(courseId: String) {
        context.preferenceDataStore.edit { preferences ->
            val updatedCourses = preferences[PURCHASED_COURSES_KEY]?.toMutableSet() ?: mutableSetOf()
            updatedCourses.add(courseId)
            preferences[PURCHASED_COURSES_KEY] = updatedCourses
        }
    }

    // Check if a Course is Purchased
    suspend fun isCoursePurchased(courseId: String): Boolean {
        val courses = context.preferenceDataStore.data.map { preferences ->
            preferences[PURCHASED_COURSES_KEY] ?: emptySet()
        }.first() // Get the first value from the Flow
        return courseId in courses
    }
    /**
     * Save JWT token to DataStore
     */
    suspend fun saveJwtToken(token: String) {
        context.preferenceDataStore.edit { preferences ->
            preferences[JWT_TOKEN_KEY] = token
        }
        Log.e("TOkkk","$token")
    }

    // Function to log in and store the JWT token in DataStore
    suspend fun loginAndStoreToken(context: Context, email: String, password: String): Boolean {
        val api = ApiService.createAuthenticatedApi(context)  // Use the authenticated API instance
        val response = api.login(LoginRequest(email, password))

        if (response.isSuccessful && response.body()?.success == true) {
            response.body()?.token?.let { token ->
                DataStoreManger(context).saveJwtToken(token) // Store JWT token
            }
            return true
        }
        return false
    }
    /**
     * Retrieve JWT token as a Flow
     */
    fun getStoredToken(): Flow<String?> {
        return context.preferenceDataStore.data.map { preferences ->
            preferences[JWT_TOKEN_KEY]
        }
    }
    suspend fun clearProfileImage(){
        context.preferenceDataStore.edit { preferences->
            preferences.remove(PROFILE_IMAGE)
        }
    }

    /**
     * Logout user and clear JWT token
     */
    suspend fun clearJwtToken() {
        context.preferenceDataStore.edit { preferences ->
            preferences.remove(JWT_TOKEN_KEY)
        }
    }
}