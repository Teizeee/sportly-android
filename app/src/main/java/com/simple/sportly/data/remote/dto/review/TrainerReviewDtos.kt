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
