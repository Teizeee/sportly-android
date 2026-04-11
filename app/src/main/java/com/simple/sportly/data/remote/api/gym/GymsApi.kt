package com.simple.sportly.data.remote.api.gym

import com.simple.sportly.data.remote.dto.gym.GymResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface GymsApi {
    @GET("api/v1/gyms")
    suspend fun getGyms(
        @Query("name") name: String? = null,
        @Query("city") city: String? = null,
        @Query("rating") rating: Int? = null
    ): List<GymResponseDto>
}
