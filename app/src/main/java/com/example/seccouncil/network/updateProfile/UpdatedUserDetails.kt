package com.example.seccouncil.network.updateProfile

data class UpdatedUserDetails(
    val __v: Int,
    val _id: String,
    val accountType: String,
    val active: Boolean,
    val additionalDetails: AdditionalDetails,
    val approved: Boolean,
    val contactNumber: String,
    val courseProgress: List<String>,
    val courses: List<String>,
    val createdAt: String,
    val email: String,
    val fcmToken: Any,
    val firstName: String,
    val image: String,
    val lastName: String,
    val notificationPreferences: NotificationPreferences,
    val password: String,
    val resetPasswordExpires: String,
    val token: String,
    val updatedAt: String
)