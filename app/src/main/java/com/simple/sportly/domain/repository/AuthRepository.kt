package com.simple.sportly.domain.repository

import com.simple.sportly.domain.model.AuthSession

interface AuthRepository {
    suspend fun login(email: String, password: String): AuthSession
    suspend fun register(
        firstName: String,
        lastName: String,
        patronymic: String?,
        email: String,
        password: String
    )
}
