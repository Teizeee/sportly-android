package com.simple.sportly.domain.repository

import com.simple.sportly.domain.model.Gym

interface GymRepository {
    suspend fun getGyms(
        name: String? = null,
        city: String? = null,
        rating: Int? = null
    ): List<Gym>
}
