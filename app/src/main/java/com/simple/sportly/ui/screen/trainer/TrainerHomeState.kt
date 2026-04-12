package com.simple.sportly.ui.screen.trainer

import com.simple.sportly.domain.model.TrainerReview
import com.simple.sportly.domain.model.TrainerSlot
import java.time.LocalDate

enum class TrainerTab {
    Schedule,
    Awards,
    Profile
}

data class TrainerHomeUiState(
    val selectedTab: TrainerTab = TrainerTab.Profile,
    val isLoading: Boolean = true,
    val isReviewsLoading: Boolean = false,
    val isScheduleLoading: Boolean = false,
    val isScheduleActionLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isUploadingAvatar: Boolean = false,
    val userId: String = "",
    val trainerId: String? = null,
    val firstName: String = "",
    val lastName: String = "",
    val patronymic: String = "",
    val birthDate: String = "",
    val email: String = "",
    val phone: String = "",
    val description: String = "",
    val selectedScheduleDate: LocalDate = LocalDate.now(),
    val slots: List<TrainerSlot> = emptyList(),
    val hasLoadedSchedule: Boolean = false,
    val scheduleErrorMessage: String? = null,
    val scheduleInfoMessage: String? = null,
    val reviews: List<TrainerReview> = emptyList(),
    val reviewsErrorMessage: String? = null,
    val avatarVersion: Long = 0L,
    val errorMessage: String? = null,
    val infoMessage: String? = null
)

