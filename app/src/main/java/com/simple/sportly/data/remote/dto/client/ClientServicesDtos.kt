package com.simple.sportly.data.remote.dto.client

import com.google.gson.annotations.SerializedName

data class ActiveClientServicesDto(
    @SerializedName("active_membership")
    val activeMembership: ClientMembershipDto?,
    @SerializedName("active_package")
    val activePackage: UserTrainerPackageDto?
)

data class MembershipServiceDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("duration_months")
    val durationMonths: Int
)

data class TrainerUserDto(
    @SerializedName("first_name")
    val firstName: String?,
    @SerializedName("last_name")
    val lastName: String?,
    @SerializedName("patronymic")
    val patronymic: String?
)

data class TrainerInfoDto(
    @SerializedName("user")
    val user: TrainerUserDto?
)

data class TrainerPackageServiceDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String?,
    @SerializedName("session_count")
    val sessionCount: Int,
    @SerializedName("trainer")
    val trainer: TrainerInfoDto?
)

data class ClientMembershipDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("membership_type_id")
    val membershipTypeId: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("purchased_at")
    val purchasedAt: String,
    @SerializedName("activated_at")
    val activatedAt: String?,
    @SerializedName("expires_at")
    val expiresAt: String?,
    @SerializedName("service")
    val service: MembershipServiceDto
)

data class UserTrainerPackageDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("trainer_package_id")
    val trainerPackageId: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("sessions_left")
    val sessionsLeft: Int,
    @SerializedName("purchased_at")
    val purchasedAt: String,
    @SerializedName("activated_at")
    val activatedAt: String?,
    @SerializedName("expires_at")
    val expiresAt: String?,
    @SerializedName("service")
    val service: TrainerPackageServiceDto
)

data class ClientProgressCreateDto(
    @SerializedName("weight")
    val weight: Double,
    @SerializedName("height")
    val height: Double,
    @SerializedName("bmi")
    val bmi: Double?
)

data class ClientProgressDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("weight")
    val weight: String,
    @SerializedName("height")
    val height: String,
    @SerializedName("bmi")
    val bmi: String,
    @SerializedName("recorded_at")
    val recordedAt: String
)

data class ClientBookingsDto(
    @SerializedName("upcoming")
    val upcoming: List<ClientBookingItemDto>,
    @SerializedName("past")
    val past: List<ClientBookingItemDto>
)

data class ClientBookingItemDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("start_time")
    val startTime: String,
    @SerializedName("end_time")
    val endTime: String,
    @SerializedName("gym")
    val gym: BookingGymShortDto,
    @SerializedName("trainer")
    val trainer: BookingTrainerShortDto
)

data class BookingGymShortDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val title: String
)

data class BookingTrainerShortDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("patronymic")
    val patronymic: String?
)
