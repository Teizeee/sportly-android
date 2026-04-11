package com.simple.sportly.data.repository

import com.simple.sportly.data.remote.api.gym.GymsApi
import com.simple.sportly.data.remote.dto.gym.GymScheduleEntryDto
import com.simple.sportly.data.remote.dto.gym.GymResponseDto
import com.simple.sportly.data.remote.dto.gym.MembershipTypeDto
import com.simple.sportly.data.remote.dto.gym.TrainerPackageDto
import com.simple.sportly.domain.model.Gym
import com.simple.sportly.domain.model.GymMembershipType
import com.simple.sportly.domain.model.GymScheduleEntry
import com.simple.sportly.domain.model.GymTrainerPackage
import com.simple.sportly.domain.repository.GymRepository

class GymRepositoryImpl(
    private val gymsApi: GymsApi
) : GymRepository {
    override suspend fun getGyms(
        name: String?,
        city: String?,
        rating: Int?
    ): List<Gym> {
        return gymsApi.getGyms(
            name = name,
            city = city,
            rating = rating
        ).map { it.toDomain() }
    }

    private fun GymResponseDto.toDomain(): Gym {
        return Gym(
            id = id,
            title = gymApplication?.title.orEmpty().ifBlank { "Gym" },
            address = gymApplication?.address.orEmpty().ifBlank { "Address is unavailable" },
            description = gymApplication?.description,
            phone = gymApplication?.phone,
            rating = rating,
            schedule = schedule.orEmpty().map { it.toDomain() },
            membershipTypes = membershipTypes.orEmpty().map { it.toDomain() },
            trainerPackages = trainerPackages.orEmpty().map { it.toDomain() }
        )
    }

    private fun GymScheduleEntryDto.toDomain(): GymScheduleEntry {
        return GymScheduleEntry(
            dayOfWeek = dayOfWeek,
            openTime = openTime,
            closeTime = closeTime
        )
    }

    private fun MembershipTypeDto.toDomain(): GymMembershipType {
        return GymMembershipType(
            id = id,
            name = name,
            description = description,
            price = price,
            durationMonths = durationMonths
        )
    }

    private fun TrainerPackageDto.toDomain(): GymTrainerPackage {
        val trainerName = listOfNotNull(
            trainer?.user?.lastName?.takeIf { it.isNotBlank() },
            trainer?.user?.firstName?.takeIf { it.isNotBlank() }
        ).joinToString(" ").ifBlank { null }

        return GymTrainerPackage(
            id = id,
            trainerId = trainerId,
            trainerFullName = trainerName,
            name = name,
            description = description,
            sessionCount = sessionCount,
            price = price
        )
    }
}
