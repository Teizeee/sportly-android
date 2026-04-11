package com.simple.sportly.domain.repository

import com.simple.sportly.domain.model.GymTrainer

interface GymTrainersRepository {
    suspend fun getGymTrainers(gymId: String): List<GymTrainer>
}
