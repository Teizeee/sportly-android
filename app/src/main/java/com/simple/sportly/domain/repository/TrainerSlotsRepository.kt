package com.simple.sportly.domain.repository

import com.simple.sportly.domain.model.TrainerSlot

interface TrainerSlotsRepository {
    suspend fun getTrainerSlotsByDate(trainerId: String, date: String): List<TrainerSlot>
    suspend fun openTrainerDay(trainerId: String, date: String): List<TrainerSlot>
    suspend fun closeTrainerDay(trainerId: String, date: String): Int
    suspend fun openTrainerSlot(trainerId: String, startTime: String, endTime: String): TrainerSlot
    suspend fun closeTrainerSlot(trainerId: String, slotId: String)
}
