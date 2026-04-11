package com.simple.sportly.data.remote.api.slots

import com.simple.sportly.data.remote.dto.slots.TrainerSlotAvailabilityDto
import com.simple.sportly.data.remote.dto.slots.TrainerSlotCreateRequestDto
import com.simple.sportly.data.remote.dto.slots.TrainerSlotDayDeleteResultDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface TrainerSlotsApi {
    @GET("api/v1/trainers/{trainer_id}/slots")
    suspend fun getTrainerSlotsByDate(
        @Path("trainer_id") trainerId: String,
        @Query("date") date: String
    ): List<TrainerSlotAvailabilityDto>

    @POST("api/v1/trainers/{trainer_id}/slots/day")
    suspend fun createTrainerSlotsForDay(
        @Path("trainer_id") trainerId: String,
        @Query("date") date: String
    ): List<TrainerSlotAvailabilityDto>

    @DELETE("api/v1/trainers/{trainer_id}/slots/day")
    suspend fun deleteTrainerSlotsForDay(
        @Path("trainer_id") trainerId: String,
        @Query("date") date: String
    ): TrainerSlotDayDeleteResultDto

    @POST("api/v1/trainers/{trainer_id}/slots")
    suspend fun createTrainerSlot(
        @Path("trainer_id") trainerId: String,
        @Body request: TrainerSlotCreateRequestDto
    ): TrainerSlotAvailabilityDto

    @DELETE("api/v1/trainers/{trainer_id}/slots/{slot_id}")
    suspend fun deleteTrainerSlot(
        @Path("trainer_id") trainerId: String,
        @Path("slot_id") slotId: String
    )
}
