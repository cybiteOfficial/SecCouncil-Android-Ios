package com.example.seccouncil.network.getEnrolledCourse

data class CourseContent(
    val __v: Int,
    val _id: String,
    val sectionName: String,
    val subSection: List<SubSection>
)