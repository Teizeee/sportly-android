package com.simple.sportly.data.remote.api.review

import com.simple.sportly.data.remote.dto.review.GymReviewCreateDto
import com.simple.sportly.data.remote.dto.review.GymReviewResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface GymReviewsApi {
    @GET("api/v1/gyms/{gym_id}/reviews")
    suspend fun getGymReviews(
        @Path("gym_id") gymId: String
    ): List<GymReviewResponseDto>

    @POST("api/v1/clients/me/reviews/gyms")
    suspend fun createGymReview(
        @Body payload: GymReviewCreateDto
    )
}
