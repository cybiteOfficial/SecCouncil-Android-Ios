package com.example.seccouncil.network.repository

import com.example.seccouncil.network.ApiInterface
import com.example.seccouncil.network.ResetPassword.ResetPasswordRequest
import com.example.seccouncil.network.ResetPassword.ResetPasswordResponse

class AuthRepository(private val api: ApiInterface) {

    // Reset Password method
    suspend fun resetPassword(email: String): Result<ResetPasswordResponse>{
        return try{
            val response = api.resetPassword(ResetPasswordRequest(email))
            if(response.isSuccessful){
                Result.success(response.body()!!)
            }else{
                Result.failure(Exception(response.errorBody()?.string()))
            }
        }catch (e: Exception){
            Result.failure(e)
        }
    }
}