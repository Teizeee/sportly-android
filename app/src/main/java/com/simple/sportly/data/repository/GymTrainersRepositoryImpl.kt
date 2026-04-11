package com.simple.sportly.data.repository

import com.simple.sportly.data.remote.api.gym.GymTrainersApi
import com.simple.sportly.data.remote.dto.gym.GymTrainerResponseDto
import com.simple.sportly.domain.model.GymTrainer
import com.simple.sportly.domain.repository.GymTrainersRepository

class GymTrainersRepositoryImpl(
    private val gymTrainersApi: GymTrainersApi
) : GymTrainersRepository {
    override suspend fun getGymTrainers(gymId: String): List<GymTrainer> {
        return gymTrainersApi.getGymTrainers(gymId).map { it.toDomain() }
    }

    private fun GymTrainerResponseDto.toDomain(): GymTrainer {
        val fullName = listOfNotNull(
            user?.lastName?.takeIf { it.isNotBlank() },
            user?.firstName?.takeIf { it.isNotBlank() }
        ).joinToString(" ").ifBlank { "Тренер" }

        return GymTrainer(
            id = id,
            userId = userId,
            fullName = fullName,
            description = description,
            rating = rating
        )
    }
}
