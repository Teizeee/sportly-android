package com.simple.sportly.data.repository

import com.simple.sportly.data.remote.api.review.GymReviewsApi
import com.simple.sportly.data.remote.dto.review.GymReviewResponseDto
import com.simple.sportly.domain.model.GymReview
import com.simple.sportly.domain.repository.GymReviewsRepository

class GymReviewsRepositoryImpl(
    private val gymReviewsApi: GymReviewsApi
) : GymReviewsRepository {
    override suspend fun getGymReviews(gymId: String): List<GymReview> {
        return gymReviewsApi.getGymReviews(gymId).map { it.toDomain() }
    }

    private fun GymReviewResponseDto.toDomain(): GymReview {
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

        return GymReview(
            id = id,
            rating = rating,
            comment = comment,
            authorFullName = displayName
        )
    }
}
