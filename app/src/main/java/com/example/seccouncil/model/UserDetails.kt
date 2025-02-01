package com.example.seccouncil.model

data class UserDetails(
val emailAddress: String = "",
val mobileNumber: String = "",
val password: String = "",
val name: String = "",
)

data class UserProfilePhoto(
    val photo: String
)

