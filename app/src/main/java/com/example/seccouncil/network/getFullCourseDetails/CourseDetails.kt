package com.example.seccouncil.network.getFullCourseDetails

data class CourseDetails(
    val __v: Int,
    val _id: String,
    val category: Category,
    val courseContent: List<CourseContent>,
    val courseDescription: String,
    val courseName: String,
    val createdAt: String,
    val instructions: List<String>,
    val instructor: Instructor,
    val price: Int,
    val ratingAndReviews: List<RatingAndReview>,
    val status: String,
    val studentsEnrolled: List<String>,
    val tag: List<String>,
    val thumbnail: String,
    val whatYouWillLearn: String
)