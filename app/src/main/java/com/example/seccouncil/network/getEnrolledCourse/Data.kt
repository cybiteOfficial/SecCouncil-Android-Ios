package com.example.seccouncil.network.getEnrolledCourse

data class Data(
    val __v: Int,
    val _id: String,
    val category: String,
    val courseContent: List<CourseContent>,
    val courseDescription: String,
    val courseName: String,
    val createdAt: String,
    val instructions: List<String>,
    val instructor: String,
    val price: Int,
    val progressPercentage: Int,
    val ratingAndReviews: List<String>,
    val status: String,
    val studentsEnrolled: List<String>,
    val tag: List<String>,
    val thumbnail: String,
    val totalDuration: String,
    val whatYouWillLearn: String
)