package com.example.seccouncil.network.getFullCourseDetails

data class Instructor(
    val __v: Int,
    val _id: String,
    val accountType: String,
    val active: Boolean,
    val additionalDetails: AdditionalDetails,
    val approved: Boolean,
    val courseProgress: List<Any>,
    val courses: List<String>,
    val createdAt: String,
    val email: String,
    val firstName: String,
    val image: String,
    val lastName: String,
    val password: String,
    val resetPasswordExpires: String,
    val token: String,
    val updatedAt: String
)