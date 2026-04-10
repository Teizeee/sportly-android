package com.simple.sportly.domain.model

data class UserProfile(
    val id: String,
    val firstName: String,
    val lastName: String,
    val patronymic: String?,
    val birthDate: String?,
    val email: String,
    val role: UserRole,
    val phone: String?,
    val description: String?
)
