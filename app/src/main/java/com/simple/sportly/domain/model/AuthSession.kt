package com.simple.sportly.domain.model

data class AuthSession(
    val token: String,
    val role: UserRole
)
