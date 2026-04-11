package com.simple.sportly.data.remote.api.gym

import com.simple.sportly.data.remote.dto.gym.GymTrainerResponseDto
import retrofit2.http.GET
import retrofit2.http.Path

interface GymTrainersApi {
    @GET("api/v1/gyms/{gym_id}/trainers")
    suspend fun getGymTrainers(
        @Path("gym_id") gymId: String
    ): List<GymTrainerResponseDto>
}
