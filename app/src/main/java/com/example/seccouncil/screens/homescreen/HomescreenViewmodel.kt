package com.example.seccouncil.screens.homescreen

import android.content.ContentResolver
import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.seccouncil.utlis.DataStoreManger
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
    }

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