package com.simple.sportly.domain.repository

import com.simple.sportly.domain.model.UserProfile

interface ProfileRepository {
    suspend fun getMyProfile(): UserProfile
    suspend fun updateMyProfile(
        firstName: String,
        lastName: String,
        patronymic: String?,
        birthDate: String?,
        email: String,
        phone: String?,
        description: String?
    ): UserProfile

    suspend fun uploadAvatar(
        bytes: ByteArray,
        fileName: String,
        mimeType: String
    )
}
