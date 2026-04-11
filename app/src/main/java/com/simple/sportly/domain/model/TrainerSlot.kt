package com.simple.sportly.domain.model

data class BookedUserShort(
    val id: String,
    val firstName: String,
    val lastName: String,
    val patronymic: String?
)

data class TrainerSlot(
    val id: String?,
    val trainerId: String,
    val startTime: String,
    val endTime: String,
    val createdAt: String?,
    val bookingId: String?,
    val bookingStatus: String?,
    val bookedUser: BookedUserShort?
)
