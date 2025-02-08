package com.example.seccouncil.network.getCourseDetail

data class CourseContent(
    val __v: Int,
    val _id: String,
    val sectionName: String,
    val subSection: List<SubSection>
)