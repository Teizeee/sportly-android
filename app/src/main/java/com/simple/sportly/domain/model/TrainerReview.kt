package com.simple.sportly.domain.model

data class TrainerReview(
    val id: String,
    val rating: Int,
    val comment: String?,
    val createdAt: String,
    val authorFullName: String
)
