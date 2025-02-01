package com.example.seccouncil.utlis


import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.seccouncil.model.UserDetails
import kotlinx.coroutines.flow.Flow
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
        val IMAGE_FILE_NAME = stringPreferencesKey("image_file_name")
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
        }
    }

    // Function to retrieve user details from DataStore
    // Returns a Flow (reactive stream) of UserDetails
    fun getFromDataStore() = context.preferenceDataStore.data.map {
        // Transform the preferences data into a UserDetails object
        // The ?: operator provides a default empty string if the value is null
        UserDetails(
            emailAddress = it[EMAIL] ?: "",
            name = it[NAME] ?: "",
            mobileNumber = it[MOBILE_NUMBER] ?: ""
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
}