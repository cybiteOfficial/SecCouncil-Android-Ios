package com.example.seccouncil.network.updateProfile

data class updateProfileRequest(
    val about: String,
    val contactNumber: Long,
    val dateOfBirth: String,
    val firstName: String,
    val gender: String,
    val lastName: String
)