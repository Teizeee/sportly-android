package com.simple.sportly.data.remote.dto.gym

import com.google.gson.annotations.SerializedName

data class GymTrainerUserDto(
    @SerializedName("first_name")
    val firstName: String?,
    @SerializedName("last_name")
    val lastName: String?
)

data class GymTrainerResponseDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("rating")
    val rating: Double?,
    @SerializedName("user")
    val user: GymTrainerUserDto?
)
