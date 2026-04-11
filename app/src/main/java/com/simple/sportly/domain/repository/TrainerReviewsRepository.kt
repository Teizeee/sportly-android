package com.simple.sportly.domain.repository

import com.simple.sportly.domain.model.TrainerReview

interface TrainerReviewsRepository {
    suspend fun getTrainerReviews(trainerId: String): List<TrainerReview>
}
