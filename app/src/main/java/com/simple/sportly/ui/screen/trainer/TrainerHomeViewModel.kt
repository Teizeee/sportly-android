package com.simple.sportly.ui.screen.trainer

import android.content.ContentResolver
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.simple.sportly.domain.model.TrainerReview
import com.simple.sportly.domain.model.TrainerSlot
import com.simple.sportly.domain.repository.ProfileRepository
import com.simple.sportly.domain.repository.TrainerReviewsRepository
import com.simple.sportly.domain.repository.TrainerSlotsRepository
import java.time.Clock
import java.time.LocalDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TrainerHomeViewModel(
    private val profileRepository: ProfileRepository,
    private val trainerReviewsRepository: TrainerReviewsRepository,
    private val trainerSlotsRepository: TrainerSlotsRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(TrainerHomeUiState())
    val uiState: StateFlow<TrainerHomeUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    fun selectTab(tab: TrainerTab) {
        _uiState.update { it.copy(selectedTab = tab) }
        when (tab) {
            TrainerTab.Awards -> loadReviewsIfNeeded()
            TrainerTab.Schedule -> Unit
            TrainerTab.Profile -> Unit
        }
    }

    fun onFirstNameChanged(value: String) {
        _uiState.update { it.copy(firstName = value, errorMessage = null, infoMessage = null) }
    }

    fun onLastNameChanged(value: String) {
        _uiState.update { it.copy(lastName = value, errorMessage = null, infoMessage = null) }
    }

    fun onPatronymicChanged(value: String) {
        _uiState.update { it.copy(patronymic = value, errorMessage = null, infoMessage = null) }
    }

    fun onBirthDateChanged(value: String) {
        _uiState.update { it.copy(birthDate = value, errorMessage = null, infoMessage = null) }
    }

    fun onEmailChanged(value: String) {
        _uiState.update { it.copy(email = value, errorMessage = null, infoMessage = null) }
    }

    fun onPhoneChanged(value: String) {
        _uiState.update { it.copy(phone = value, errorMessage = null, infoMessage = null) }
    }

    fun onDescriptionChanged(value: String) {
        _uiState.update { it.copy(description = value, errorMessage = null, infoMessage = null) }
    }

    fun saveProfile() {
        val state = _uiState.value
        if (state.firstName.isBlank() || state.lastName.isBlank() || state.email.isBlank()) {
            _uiState.update { it.copy(errorMessage = "First name, last name and email are required.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, errorMessage = null, infoMessage = null) }
            runCatching {
                profileRepository.updateMyProfile(
                    firstName = state.firstName.trim(),
                    lastName = state.lastName.trim(),
                    patronymic = state.patronymic.trim().ifBlank { null },
                    birthDate = state.birthDate.trim().ifBlank { null },
                    email = state.email.trim(),
                    phone = state.phone.trim().ifBlank { null },
                    description = state.description.trim().ifBlank { null }
                )
            }.onSuccess { profile ->
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        userId = profile.id,
                        firstName = profile.firstName,
                        lastName = profile.lastName,
                        patronymic = profile.patronymic.orEmpty(),
                        birthDate = profile.birthDate.orEmpty(),
                        email = profile.email,
                        phone = profile.phone.orEmpty(),
                        description = profile.description.orEmpty(),
                        infoMessage = "Profile saved."
                    )
                }
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        errorMessage = throwable.message ?: "Failed to save profile."
                    )
                }
            }
        }
    }

    fun uploadAvatar(uri: Uri, contentResolver: ContentResolver) {
        viewModelScope.launch {
            _uiState.update { it.copy(isUploadingAvatar = true, errorMessage = null, infoMessage = null) }
            runCatching {
                val bytes = withContext(Dispatchers.IO) {
                    contentResolver.openInputStream(uri)?.use { it.readBytes() }
                } ?: throw IllegalStateException("Cannot read selected image.")

                val mimeType = contentResolver.getType(uri) ?: "image/jpeg"
                val extension = mimeType.substringAfter('/', "jpg")
                val fileName = "avatar.$extension"

                profileRepository.uploadAvatar(
                    bytes = bytes,
                    fileName = fileName,
                    mimeType = mimeType
                )
            }.onSuccess {
                _uiState.update {
                    it.copy(
                        isUploadingAvatar = false,
                        avatarVersion = System.currentTimeMillis(),
                        infoMessage = "Avatar updated."
                    )
                }
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(
                        isUploadingAvatar = false,
                        errorMessage = throwable.message ?: "Failed to upload avatar."
                    )
                }
            }
        }
    }

    fun loadProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, infoMessage = null) }
            runCatching { profileRepository.getMyProfile() }
                .onSuccess { profile ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            userId = profile.id,
                            trainerId = profile.trainerId,
                            firstName = profile.firstName,
                            lastName = profile.lastName,
                            patronymic = profile.patronymic.orEmpty(),
                            birthDate = profile.birthDate.orEmpty(),
                            email = profile.email,
                            phone = profile.phone.orEmpty(),
                            description = profile.description.orEmpty()
                        )
                    }
                    loadReviews(force = true)
                    loadScheduleForSelectedDate()
                }
                .onFailure { throwable ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = throwable.message ?: "Failed to load profile."
                        )
                    }
                }
        }
    }

    fun loadReviewsIfNeeded() {
        val state = _uiState.value
        if (state.isReviewsLoading || state.reviews.isNotEmpty()) return
        loadReviews(force = false)
    }

    fun loadReviews(force: Boolean) {
        val trainerId = _uiState.value.trainerId
        if (trainerId.isNullOrBlank()) {
            _uiState.update {
                it.copy(
                    reviews = emptyList(),
                    reviewsErrorMessage = "Не удалось определить ID тренера."
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isReviewsLoading = true,
                    reviewsErrorMessage = if (force) null else it.reviewsErrorMessage
                )
            }

            runCatching { trainerReviewsRepository.getTrainerReviews(trainerId) }
                .onSuccess { reviews ->
                    _uiState.update {
                        it.copy(
                            isReviewsLoading = false,
                            reviews = reviews,
                            reviewsErrorMessage = null
                        )
                    }
                }
                .onFailure { throwable ->
                    _uiState.update {
                        it.copy(
                            isReviewsLoading = false,
                            reviewsErrorMessage = throwable.message ?: "Не удалось загрузить отзывы."
                        )
                    }
                }
        }
    }

    fun onScheduleDateSelected(date: LocalDate) {
        if (_uiState.value.selectedScheduleDate == date) return
        _uiState.update {
            it.copy(
                selectedScheduleDate = date,
                scheduleErrorMessage = null,
                scheduleInfoMessage = null
            )
        }
        loadScheduleForSelectedDate()
    }

    fun loadScheduleIfNeeded() {
        val state = _uiState.value
        if (state.isScheduleLoading || state.hasLoadedSchedule) return
        loadScheduleForSelectedDate()
    }

    fun loadScheduleForSelectedDate() {
        val state = _uiState.value
        val trainerId = state.trainerId
        if (trainerId.isNullOrBlank()) {
            _uiState.update {
                it.copy(
                    scheduleErrorMessage = "Не удалось определить ID тренера."
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isScheduleLoading = true,
                    scheduleErrorMessage = null
                )
            }
            runCatching {
                withContext(Dispatchers.IO) {
                    trainerSlotsRepository.getTrainerSlotsByDate(
                        trainerId = trainerId,
                        date = state.selectedScheduleDate.toString()
                    )
                }
            }.onSuccess { slots ->
                _uiState.update {
                    it.copy(
                        isScheduleLoading = false,
                        hasLoadedSchedule = true,
                        slots = slots,
                        scheduleErrorMessage = null
                    )
                }
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(
                        isScheduleLoading = false,
                        hasLoadedSchedule = true,
                        scheduleErrorMessage = throwable.message ?: "Не удалось загрузить расписание."
                    )
                }
            }
        }
    }

    fun openSelectedDay() {
        val state = _uiState.value
        if (isPastDate(state.selectedScheduleDate)) {
            _uiState.update {
                it.copy(
                    scheduleInfoMessage = "Прошедшие даты доступны только для просмотра.",
                    scheduleErrorMessage = null
                )
            }
            return
        }
        val trainerId = state.trainerId ?: return

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isScheduleActionLoading = true,
                    scheduleErrorMessage = null,
                    scheduleInfoMessage = null
                )
            }

            runCatching {
                withContext(Dispatchers.IO) {
                    trainerSlotsRepository.openTrainerDay(
                        trainerId = trainerId,
                        date = state.selectedScheduleDate.toString()
                    )
                }
            }.onSuccess { slots ->
                _uiState.update {
                    it.copy(
                        isScheduleActionLoading = false,
                        slots = slots,
                        hasLoadedSchedule = true,
                        scheduleInfoMessage = "День открыт."
                    )
                }
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(
                        isScheduleActionLoading = false,
                        scheduleErrorMessage = throwable.message ?: "Не удалось открыть день."
                    )
                }
            }
        }
    }

    fun closeSelectedDay() {
        val state = _uiState.value
        if (isPastDate(state.selectedScheduleDate)) {
            _uiState.update {
                it.copy(
                    scheduleInfoMessage = "Прошедшие даты доступны только для просмотра.",
                    scheduleErrorMessage = null
                )
            }
            return
        }
        val trainerId = state.trainerId ?: return

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isScheduleActionLoading = true,
                    scheduleErrorMessage = null,
                    scheduleInfoMessage = null
                )
            }

            runCatching {
                withContext(Dispatchers.IO) {
                    trainerSlotsRepository.closeTrainerDay(
                        trainerId = trainerId,
                        date = state.selectedScheduleDate.toString()
                    )
                }
            }.onSuccess {
                _uiState.update {
                    it.copy(
                        isScheduleActionLoading = false,
                        slots = state.slots.map { slot ->
                            if (slot.bookingId == null && slot.id != null) {
                                slot.copy(id = null, createdAt = null)
                            } else {
                                slot
                            }
                        },
                        hasLoadedSchedule = true,
                        scheduleInfoMessage = "День закрыт."
                    )
                }
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(
                        isScheduleActionLoading = false,
                        scheduleErrorMessage = throwable.message ?: "Не удалось закрыть день."
                    )
                }
            }
        }
    }

    fun toggleSlotAvailability(slot: TrainerSlot) {
        if (slot.bookingId != null) return
        val state = _uiState.value
        if (isPastDate(state.selectedScheduleDate)) {
            _uiState.update {
                it.copy(
                    scheduleInfoMessage = "Прошедшие даты доступны только для просмотра.",
                    scheduleErrorMessage = null
                )
            }
            return
        }
        val trainerId = state.trainerId ?: return

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isScheduleActionLoading = true,
                    scheduleErrorMessage = null,
                    scheduleInfoMessage = null
                )
            }

            runCatching {
                if (slot.id == null) {
                    withContext(Dispatchers.IO) {
                        trainerSlotsRepository.openTrainerSlot(
                            trainerId = trainerId,
                            startTime = slot.startTime,
                            endTime = slot.endTime
                        )
                    }
                } else {
                    withContext(Dispatchers.IO) {
                        trainerSlotsRepository.closeTrainerSlot(
                            trainerId = trainerId,
                            slotId = slot.id
                        )
                    }
                    null
                }
            }.onSuccess { createdSlot ->
                _uiState.update {
                    val updatedSlots = if (slot.id == null) {
                        val openedSlot = createdSlot ?: slot
                        state.slots.map { current ->
                            if (current.startTime == slot.startTime && current.endTime == slot.endTime) {
                                openedSlot
                            } else {
                                current
                            }
                        }
                    } else {
                        state.slots.map { current ->
                            if (current.id == slot.id) {
                                current.copy(id = null, createdAt = null)
                            } else {
                                current
                            }
                        }
                    }
                    it.copy(
                        isScheduleActionLoading = false,
                        slots = updatedSlots,
                        hasLoadedSchedule = true,
                        scheduleInfoMessage = if (slot.id == null) {
                            "Слот открыт."
                        } else {
                            "Слот закрыт."
                        }
                    )
                }
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(
                        isScheduleActionLoading = false,
                        scheduleErrorMessage = throwable.message ?: "Не удалось изменить слот."
                    )
                }
            }
        }
    }

    private fun isPastDate(date: LocalDate): Boolean {
        return date.isBefore(LocalDate.now(Clock.systemDefaultZone()))
    }

    companion object {
        fun factory(
            profileRepository: ProfileRepository,
            trainerReviewsRepository: TrainerReviewsRepository,
            trainerSlotsRepository: TrainerSlotsRepository
        ): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return TrainerHomeViewModel(
                        profileRepository = profileRepository,
                        trainerReviewsRepository = trainerReviewsRepository,
                        trainerSlotsRepository = trainerSlotsRepository
                    ) as T
                }
            }
    }
}
