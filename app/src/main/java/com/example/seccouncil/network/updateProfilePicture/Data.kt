package com.example.seccouncil.network.updateProfilePicture

data class Data(
    val __v: Int,
    val _id: String,
    val accountType: String,
    val active: Boolean,
    val additionalDetails: String,
    val approved: Boolean,
    val contactNumber: String,
    val courseProgress: List<Any>,
    val courses: List<Any>,
    val createdAt: String,
    val email: String,
    val firstName: String,
    val image: String,
    val lastName: String,
    val password: String,
    val updatedAt: String
)