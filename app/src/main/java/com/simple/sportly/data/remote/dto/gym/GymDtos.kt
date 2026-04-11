package com.simple.sportly.data.remote.dto.gym

import com.google.gson.annotations.SerializedName

data class GymApplicationDto(
    @SerializedName("title")
    val title: String?,
    @SerializedName("address")
    val address: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("phone")
    val phone: String?
)

data class GymScheduleEntryDto(
    @SerializedName("day_of_week")
    val dayOfWeek: Int,
    @SerializedName("open_time")
    val openTime: String?,
    @SerializedName("close_time")
    val closeTime: String?
)

data class TrainerUserDto(
    @SerializedName("first_name")
    val firstName: String?,
    @SerializedName("last_name")
    val lastName: String?
)

data class TrainerInfoDto(
    @SerializedName("id")
    val id: String?,
    @SerializedName("user")
    val user: TrainerUserDto?
)

data class MembershipTypeDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("price")
    val price: String,
    @SerializedName("duration_months")
    val durationMonths: Int
)

data class TrainerPackageDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("trainer_id")
    val trainerId: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String?,
    @SerializedName("session_count")
    val sessionCount: Int,
    @SerializedName("price")
    val price: String,
    @SerializedName("trainer")
    val trainer: TrainerInfoDto?
)

data class GymResponseDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("rating")
    val rating: Double?,
    @SerializedName("gym_application")
    val gymApplication: GymApplicationDto?,
    @SerializedName("schedule")
    val schedule: List<GymScheduleEntryDto>?,
    @SerializedName("membership_types")
    val membershipTypes: List<MembershipTypeDto>?,
    @SerializedName("trainer_packages")
    val trainerPackages: List<TrainerPackageDto>?
)
