package com.example.seccouncil.network.getAllCourseDetailsModel

data class Data(
    val _id: String,
    val courseName: String,
    val instructor: Instructor,
    val price: Int,
    val ratingAndReviews: List<String>,
    val studentsEnrolled: List<String>,
    val thumbnail: String
)