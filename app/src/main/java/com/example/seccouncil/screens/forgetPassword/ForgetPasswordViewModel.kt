package com.example.seccouncil.screens.forgetPassword

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.seccouncil.network.repository.AuthRepository
import kotlinx.coroutines.launch

class ForgetPasswordViewModel(private val repository: AuthRepository): ViewModel() {

    var email by mutableStateOf("")
        private set
    var loading by mutableStateOf(false)
        private set
    var message by mutableStateOf<String?>(null)
        private set
    var success by mutableStateOf(false)
        private set
    fun onEmailChange(newEmail: String){
        email = newEmail
    }
    fun submitResetPassword(){
        if(loading) return // Prevent double-tap submission
        viewModelScope.launch {
            loading = true
            val result = repository.resetPassword(email)
            loading = false
            success = true
            message = result.fold(
                onSuccess = {it.message},
                onFailure = {
                    success = false
                    it.localizedMessage?:("Unknown Errpr")
                }
            )
        }
    }
    fun onSuccessReset(value : Boolean){
        success = value
    }
    fun clearMessage() {
        message = null
    }
}

class ForgetPasswordViewModelFactory(
    private val repository: AuthRepository
): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ForgetPasswordViewModel::class.java)){
            return ForgetPasswordViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}