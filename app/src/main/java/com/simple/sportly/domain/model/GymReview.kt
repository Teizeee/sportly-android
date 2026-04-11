package com.simple.sportly.domain.model

data class GymReview(
    val id: String,
    val rating: Int,
    val comment: String?,
    val authorFullName: String
)
