package com.example.seccouncil.network.updateProfile

data class updateProfileResponse(
    val message: String,
    val success: Boolean,
    val updatedUserDetails: UpdatedUserDetails
)