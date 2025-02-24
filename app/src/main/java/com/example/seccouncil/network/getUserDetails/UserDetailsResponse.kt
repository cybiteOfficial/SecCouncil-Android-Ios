package com.example.seccouncil.network.getUserDetails

data class UserDetailsResponse(
    val data: Data,
    val message: String,
    val success: Boolean
)