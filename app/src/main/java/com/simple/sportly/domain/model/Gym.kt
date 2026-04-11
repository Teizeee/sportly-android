package com.simple.sportly.domain.model

data class Gym(
    val id: String,
    val title: String,
    val address: String,
    val description: String?,
    val phone: String?,
    val rating: Double?,
    val schedule: List<GymScheduleEntry>,
    val membershipTypes: List<GymMembershipType>,
    val trainerPackages: List<GymTrainerPackage>
)

data class GymScheduleEntry(
    val dayOfWeek: Int,
    val openTime: String?,
    val closeTime: String?
)

data class GymMembershipType(
    val id: String,
    val name: String,
    val description: String,
    val price: String,
    val durationMonths: Int
)

data class GymTrainerPackage(
    val id: String,
    val trainerId: String,
    val trainerFullName: String?,
    val name: String,
    val description: String?,
    val sessionCount: Int,
    val price: String
)
