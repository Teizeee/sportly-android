package com.simple.sportly.data.repository

import com.simple.sportly.data.remote.api.review.TrainerReviewsApi
import com.simple.sportly.data.remote.dto.review.TrainerReviewCreateDto
import com.simple.sportly.data.remote.dto.review.TrainerReviewResponseDto
import com.simple.sportly.domain.model.TrainerReview
import com.simple.sportly.domain.repository.TrainerReviewsRepository

class TrainerReviewsRepositoryImpl(
    private val trainerReviewsApi: TrainerReviewsApi
) : TrainerReviewsRepository {
    override suspend fun getTrainerReviews(trainerId: String): List<TrainerReview> {
        return trainerReviewsApi.getTrainerReviews(trainerId).map { it.toDomain() }
    }

    override suspend fun createTrainerReview(
        trainerId: String,
        rating: Int,
        comment: String?
    ) {
        trainerReviewsApi.createTrainerReview(
            payload = TrainerReviewCreateDto(
                trainerId = trainerId,
                rating = rating,
                comment = comment
            )
        )
    }

    private fun TrainerReviewResponseDto.toDomain(): TrainerReview {
        val firstName = author.firstName.trim()
        val lastInitial = author.lastName
            .trim()
            .firstOrNull()
            ?.uppercaseChar()
            ?.let { "$it." }
            .orEmpty()
        val displayName = listOf(firstName, lastInitial)
            .filter { it.isNotBlank() }
            .joinToString(" ")
            .ifBlank { firstName }

        return TrainerReview(
            id = id,
            rating = rating,
            comment = comment,
            createdAt = createdAt,
            authorFullName = displayName
        )
    }
}
