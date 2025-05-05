package com.example.seccouncil.common

import com.example.seccouncil.model.UserProfilePhoto
import kotlinx.coroutines.flow.Flow

object ProfileImage {
    lateinit var profileImage: Flow<UserProfilePhoto>
}