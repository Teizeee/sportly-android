package com.simple.sportly.domain.repository

import com.simple.sportly.domain.model.GymReview

interface GymReviewsRepository {
    suspend fun getGymReviews(gymId: String): List<GymReview>
    suspend fun createGymReview(
        gymId: String,
        rating: Int,
        comment: String?
    )
}
