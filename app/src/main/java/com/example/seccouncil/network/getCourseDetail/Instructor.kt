package com.example.seccouncil.network.getCourseDetail

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
    val updatedAt: String
)