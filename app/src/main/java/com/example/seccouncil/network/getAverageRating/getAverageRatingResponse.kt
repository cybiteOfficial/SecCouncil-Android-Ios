package com.example.seccouncil.network.getAverageRating

data class getAverageRatingResponse(
    val success: Boolean,
    val message: String?,
    val averageRating: Double?
)