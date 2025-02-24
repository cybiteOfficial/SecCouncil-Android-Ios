package com.example.seccouncil.network.getFullCourseDetails

data class CourseContent(
    val __v: Int,
    val _id: String,
    val sectionName: String,
    val subSection: List<SubSection>
)