package com.simple.sportly.domain.model

data class GymTrainer(
    val id: String,
    val userId: String,
    val fullName: String,
    val description: String,
    val rating: Double?
)
