package com.simple.sportly.data.remote.api.review

import com.simple.sportly.data.remote.dto.review.TrainerReviewResponseDto
import retrofit2.http.GET
import retrofit2.http.Path

interface TrainerReviewsApi {
    @GET("api/v1/trainers/{trainer_id}/reviews")
    suspend fun getTrainerReviews(
        @Path("trainer_id") trainerId: String
    ): List<TrainerReviewResponseDto>
}
