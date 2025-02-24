package com.example.seccouncil.network.getFullCourseDetails

data class Data(
    val completedVideos: List<Any>,
    val courseDetails: CourseDetails,
    val totalDuration: String
)