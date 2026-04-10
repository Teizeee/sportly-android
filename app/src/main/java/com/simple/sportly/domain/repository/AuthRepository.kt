package com.simple.sportly.domain.repository

import com.simple.sportly.domain.model.AuthSession
import com.simple.sportly.domain.model.UserProfile

interface AuthRepository {
    suspend fun login(email: String, password: String): AuthSession
    suspend fun register(
        firstName: String,
        lastName: String,
        patronymic: String?,
        email: String,
        password: String
    )
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
