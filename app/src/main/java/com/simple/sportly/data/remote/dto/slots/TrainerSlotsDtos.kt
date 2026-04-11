package com.simple.sportly.data.remote.dto.slots

import com.google.gson.annotations.SerializedName

data class BookedUserShortDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("patronymic")
    val patronymic: String?
)

data class TrainerSlotAvailabilityDto(
    @SerializedName("id")
    val id: String?,
    @SerializedName("trainer_id")
    val trainerId: String,
    @SerializedName("start_time")
    val startTime: String,
    @SerializedName("end_time")
    val endTime: String,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("booking_id")
    val bookingId: String?,
    @SerializedName("booking_status")
    val bookingStatus: String?,
    @SerializedName("booked_user")
    val bookedUser: BookedUserShortDto?
)

data class TrainerSlotCreateRequestDto(
    @SerializedName("start_time")
    val startTime: String,
    @SerializedName("end_time")
    val endTime: String
)

data class TrainerSlotDayDeleteResultDto(
    @SerializedName("deleted_count")
    val deletedCount: Int = 0
)
