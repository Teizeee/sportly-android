package com.simple.sportly.domain.model

data class ClientProgress(
    val id: String,
    val userId: String,
    val weight: Double,
    val height: Double,
    val bmi: Double,
    val recordedAt: String
)
