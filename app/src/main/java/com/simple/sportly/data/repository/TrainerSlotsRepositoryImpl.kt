package com.simple.sportly.data.repository

import com.simple.sportly.data.remote.api.slots.TrainerSlotsApi
import com.simple.sportly.data.remote.dto.slots.TrainerSlotAvailabilityDto
import com.simple.sportly.data.remote.dto.slots.TrainerSlotCreateRequestDto
import com.simple.sportly.domain.model.BookedUserShort
import com.simple.sportly.domain.model.TrainerSlot
import com.simple.sportly.domain.repository.TrainerSlotsRepository

class TrainerSlotsRepositoryImpl(
    private val trainerSlotsApi: TrainerSlotsApi
) : TrainerSlotsRepository {
    override suspend fun getTrainerSlotsByDate(trainerId: String, date: String): List<TrainerSlot> {
        return trainerSlotsApi.getTrainerSlotsByDate(trainerId, date).map { it.toDomain() }
    }

    override suspend fun openTrainerDay(trainerId: String, date: String): List<TrainerSlot> {
        return trainerSlotsApi.createTrainerSlotsForDay(trainerId, date).map { it.toDomain() }
    }

    override suspend fun closeTrainerDay(trainerId: String, date: String): Int {
        return trainerSlotsApi.deleteTrainerSlotsForDay(trainerId, date).deletedCount
    }

    override suspend fun openTrainerSlot(
        trainerId: String,
        startTime: String,
        endTime: String
    ): TrainerSlot {
        return trainerSlotsApi.createTrainerSlot(
            trainerId = trainerId,
            request = TrainerSlotCreateRequestDto(
                startTime = startTime,
                endTime = endTime
            )
        ).toDomain()
    }

    override suspend fun closeTrainerSlot(trainerId: String, slotId: String) {
        trainerSlotsApi.deleteTrainerSlot(trainerId = trainerId, slotId = slotId)
    }

    private fun TrainerSlotAvailabilityDto.toDomain(): TrainerSlot {
        return TrainerSlot(
            id = id,
            trainerId = trainerId,
            startTime = startTime,
            endTime = endTime,
            createdAt = createdAt,
            bookingId = bookingId,
            bookingStatus = bookingStatus,
            bookedUser = bookedUser?.let {
                BookedUserShort(
                    id = it.id,
                    firstName = it.firstName,
                    lastName = it.lastName,
                    patronymic = it.patronymic
                )
            }
        )
    }
}
