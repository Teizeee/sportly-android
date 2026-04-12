package com.simple.sportly.domain.model

data class ClientBookings(
    val upcoming: List<ClientBooking>,
    val past: List<ClientBooking>
)

data class ClientBooking(
    val id: String,
    val status: ClientBookingStatus,
    val date: String,
    val startTime: String,
    val endTime: String,
    val gymTitle: String,
    val trainerId: String,
    val trainerFirstName: String,
    val trainerLastName: String,
    val trainerPatronymic: String?
)

enum class ClientBookingStatus {
    CREATED,
    VISITED,
    NOT_VISITED,
    CANCELLED;

    companion object {
        fun fromApi(value: String): ClientBookingStatus {
            return entries.firstOrNull { it.name == value } ?: CREATED
        }
    }
}
