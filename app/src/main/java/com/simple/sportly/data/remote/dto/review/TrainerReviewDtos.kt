package com.simple.sportly.data.remote.dto.review

import com.google.gson.annotations.SerializedName

data class ReviewUserInfoDto(
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("patronymic")
    val patronymic: String?
)

data class TrainerReviewResponseDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("rating")
    val rating: Int,
    @SerializedName("comment")
    val comment: String?,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("author")
    val author: ReviewUserInfoDto
)

data class GymReviewResponseDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("rating")
    val rating: Int,
    @SerializedName("comment")
    val comment: String?,
    @SerializedName("author")
    val author: ReviewUserInfoDto
)

data class TrainerReviewCreateDto(
    @SerializedName("trainer_id")
    val trainerId: String,
    @SerializedName("rating")
    val rating: Int,
    @SerializedName("comment")
    val comment: String?
)

data class GymReviewCreateDto(
    @SerializedName("gym_id")
    val gymId: String,
    @SerializedName("rating")
    val rating: Int,
    @SerializedName("comment")
    val comment: String?
)
