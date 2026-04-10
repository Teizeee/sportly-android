package com.simple.sportly.data.remote.dto

import com.google.gson.annotations.SerializedName

data class LoginRequestDto(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String
)

data class RegisterRequestDto(
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("patronymic")
    val patronymic: String?,
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("role")
    val role: String = "CLIENT"
)

data class TokenResponseDto(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("token_type")
    val tokenType: String,
    @SerializedName("role")
    val role: String
)

data class UserResponseDto(
    @SerializedName("id")
    val id: String
)

data class TrainerProfileDto(
    @SerializedName("phone")
    val phone: String?,
    @SerializedName("description")
    val description: String?
)

data class UserProfileResponseDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("patronymic")
    val patronymic: String?,
    @SerializedName("birth_date")
    val birthDate: String?,
    @SerializedName("email")
    val email: String,
    @SerializedName("role")
    val role: String,
    @SerializedName("trainer_profile")
    val trainerProfile: TrainerProfileDto?
)

data class UserUpdateRequestDto(
    @SerializedName("first_name")
    val firstName: String?,
    @SerializedName("last_name")
    val lastName: String?,
    @SerializedName("patronymic")
    val patronymic: String?,
    @SerializedName("birth_date")
    val birthDate: String?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("phone")
    val phone: String?,
    @SerializedName("description")
    val description: String?
)
