package com.simple.sportly.ui.screen.client

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.simple.sportly.domain.model.ActiveMembership
import com.simple.sportly.domain.model.ActivePackage
import com.simple.sportly.domain.model.ClientBooking
import com.simple.sportly.domain.model.ClientMembership
import com.simple.sportly.domain.model.ClientMembershipStatus
import com.simple.sportly.domain.model.ClientTrainerPackage
import com.simple.sportly.domain.model.ClientTrainerPackageStatus
import com.simple.sportly.domain.model.Gym
import com.simple.sportly.domain.model.GymReview
import com.simple.sportly.domain.model.GymTrainer
import com.simple.sportly.domain.model.TrainerSlot
import com.simple.sportly.domain.model.TrainerReview
import com.simple.sportly.domain.repository.ClientServicesRepository
import com.simple.sportly.domain.repository.GymRepository
import com.simple.sportly.domain.repository.GymReviewsRepository
import com.simple.sportly.domain.repository.GymTrainersRepository
import com.simple.sportly.domain.repository.ProfileRepository
import com.simple.sportly.domain.repository.TrainerReviewsRepository
import com.simple.sportly.domain.repository.TrainerSlotsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.math.pow
import kotlin.math.round

class ClientHomeViewModel(
    private val profileRepository: ProfileRepository,
    private val clientServicesRepository: ClientServicesRepository,
    private val gymRepository: GymRepository,
    private val gymReviewsRepository: GymReviewsRepository,
    private val gymTrainersRepository: GymTrainersRepository,
    private val trainerReviewsRepository: TrainerReviewsRepository,
    private val trainerSlotsRepository: TrainerSlotsRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ClientHomeUiState())
    val uiState: StateFlow<ClientHomeUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
        loadActiveServices()
    }

    fun selectTab(tab: ClientTab) {
        _uiState.update {
            it.copy(
                selectedTab = tab,
                isStatisticsMembershipsOpened = if (tab == ClientTab.Statistics) {
                    it.isStatisticsMembershipsOpened
                } else {
                    false
                },
                isStatisticsPackagesOpened = if (tab == ClientTab.Statistics) {
                    it.isStatisticsPackagesOpened
                } else {
                    false
                },
                isStatisticsPackageBookingOpened = if (tab == ClientTab.Statistics) {
                    it.isStatisticsPackageBookingOpened
                } else {
                    false
                },
                isStatisticsWeightOpened = if (tab == ClientTab.Statistics) {
                    it.isStatisticsWeightOpened
                } else {
                    false
                },
                isStatisticsWeightDynamicsOpened = if (tab == ClientTab.Statistics) {
                    it.isStatisticsWeightDynamicsOpened
                } else {
                    false
                },
                isStatisticsTrainingsOpened = if (tab == ClientTab.Statistics) {
                    it.isStatisticsTrainingsOpened
                } else {
                    false
                },
                isTrainerReviewFormOpened = if (tab == ClientTab.Statistics) {
                    it.isTrainerReviewFormOpened
                } else {
                    false
                },
                isGymReviewFormOpened = if (tab == ClientTab.Statistics) {
                    it.isGymReviewFormOpened
                } else {
                    false
                },
            )
        }
        if (tab == ClientTab.Marketplace && _uiState.value.gyms.isEmpty()) {
            loadGyms()
        }
        if (
            tab == ClientTab.Statistics &&
            !_uiState.value.isActiveServicesLoading &&
            _uiState.value.activeMembership == null &&
            _uiState.value.activePackage == null
        ) {
            loadActiveServices()
        }
    }

    fun openStatisticsMemberships() {
        _uiState.update {
            it.copy(
                isStatisticsMembershipsOpened = true,
                isStatisticsPackagesOpened = false,
                isStatisticsPackageBookingOpened = false,
                isStatisticsWeightOpened = false,
                isStatisticsWeightDynamicsOpened = false,
                isStatisticsTrainingsOpened = false,
                isTrainerReviewFormOpened = false,
                isGymReviewFormOpened = false
            )
        }
        if (_uiState.value.memberships.isEmpty() && !_uiState.value.isMembershipsLoading) {
            loadMemberships()
        }
    }

    fun closeStatisticsMemberships() {
        _uiState.update { it.copy(isStatisticsMembershipsOpened = false) }
    }

    fun openStatisticsPackages() {
        _uiState.update {
            it.copy(
                isStatisticsMembershipsOpened = false,
                isStatisticsPackagesOpened = true,
                isStatisticsPackageBookingOpened = false,
                isStatisticsWeightOpened = false,
                isStatisticsWeightDynamicsOpened = false,
                isStatisticsTrainingsOpened = false,
                isTrainerReviewFormOpened = false,
                isGymReviewFormOpened = false
            )
        }
        if (_uiState.value.packages.isEmpty() && !_uiState.value.isPackagesLoading) {
            loadPackages()
        }
    }

    fun closeStatisticsPackages() {
        _uiState.update {
            it.copy(
                isStatisticsPackagesOpened = false,
                isStatisticsPackageBookingOpened = false,
                bookingPackage = null,
                bookingAvailableSlots = emptyList(),
                selectedBookingSlots = emptyList(),
                bookingSlotsErrorMessage = null,
                bookingInfoMessage = null
            )
        }
    }

    fun openStatisticsWeight() {
        _uiState.update {
            it.copy(
                isStatisticsWeightOpened = true,
                isStatisticsWeightDynamicsOpened = false,
                isStatisticsTrainingsOpened = false,
                isStatisticsPackageBookingOpened = false,
                isTrainerReviewFormOpened = false,
                isGymReviewFormOpened = false,
                progressSaveErrorMessage = null
            )
        }
    }

    fun closeStatisticsWeight() {
        _uiState.update {
            it.copy(
                isStatisticsWeightOpened = false,
                isStatisticsWeightDynamicsOpened = false
            )
        }
    }

    fun openStatisticsWeightDynamics() {
        _uiState.update { it.copy(isStatisticsWeightDynamicsOpened = true) }
        loadProgressHistory()
    }

    fun closeStatisticsWeightDynamics() {
        _uiState.update { it.copy(isStatisticsWeightDynamicsOpened = false) }
    }

    fun openStatisticsTrainings() {
        _uiState.update {
            it.copy(
                isStatisticsTrainingsOpened = true,
                isStatisticsMembershipsOpened = false,
                isStatisticsPackagesOpened = false,
                isStatisticsPackageBookingOpened = false,
                isStatisticsWeightOpened = false,
                isStatisticsWeightDynamicsOpened = false,
                isTrainerReviewFormOpened = false,
                isGymReviewFormOpened = false
            )
        }
        if (
            _uiState.value.upcomingBookings.isEmpty() &&
            _uiState.value.pastBookings.isEmpty() &&
            !_uiState.value.isBookingsLoading
        ) {
            loadBookings()
        }
    }

    fun closeStatisticsTrainings() {
        _uiState.update {
            it.copy(
                isStatisticsTrainingsOpened = false,
                isTrainerReviewFormOpened = false,
                isGymReviewFormOpened = false,
                reviewBooking = null,
                trainerReviewRating = 0,
                trainerReviewComment = "",
                shouldLeaveGymReview = false,
                gymReviewRating = 0,
                gymReviewComment = "",
                isTrainerReviewSubmitting = false,
                isGymReviewSubmitting = false,
                reviewErrorMessage = null
            )
        }
    }

    fun refreshBookings() {
        loadBookings()
    }

    fun selectMyTrainingsTab(tab: MyTrainingsTab) {
        _uiState.update { it.copy(selectedMyTrainingsTab = tab) }
    }

    fun cancelBooking(bookingId: String) {
        if (_uiState.value.cancellingBookingId != null) return
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    cancellingBookingId = bookingId,
                    bookingsErrorMessage = null
                )
            }

            runCatching { clientServicesRepository.cancelBooking(bookingId) }
                .onSuccess {
                    _uiState.update { state ->
                        state.copy(
                            cancellingBookingId = null,
                            upcomingBookings = state.upcomingBookings.filterNot { booking -> booking.id == bookingId },
                            pastBookings = state.pastBookings.filterNot { booking -> booking.id == bookingId }
                        )
                    }
                    loadBookings()
                }
                .onFailure { throwable ->
                    _uiState.update {
                        it.copy(
                            cancellingBookingId = null,
                            bookingsErrorMessage = throwable.message ?: "Не удалось отменить тренировку."
                        )
                    }
                }
        }
    }

    fun leaveBookingReview(bookingId: String) {
        val booking = (_uiState.value.pastBookings + _uiState.value.upcomingBookings)
            .firstOrNull { it.id == bookingId }
            ?: return

        _uiState.update {
            it.copy(
                isTrainerReviewFormOpened = true,
                isGymReviewFormOpened = false,
                isStatisticsTrainingsOpened = false,
                reviewBooking = booking,
                trainerReviewRating = 0,
                trainerReviewComment = "",
                shouldLeaveGymReview = false,
                gymReviewRating = 0,
                gymReviewComment = "",
                isTrainerReviewSubmitting = false,
                isGymReviewSubmitting = false,
                reviewErrorMessage = null
            )
        }
    }

    fun closeTrainerReviewForm() {
        _uiState.update {
            it.copy(
                isTrainerReviewFormOpened = false,
                isStatisticsTrainingsOpened = true,
                reviewBooking = null,
                trainerReviewRating = 0,
                trainerReviewComment = "",
                shouldLeaveGymReview = false,
                isTrainerReviewSubmitting = false,
                reviewErrorMessage = null
            )
        }
    }

    fun closeGymReviewForm() {
        _uiState.update {
            it.copy(
                isGymReviewFormOpened = false,
                isStatisticsTrainingsOpened = true,
                reviewBooking = null,
                gymReviewRating = 0,
                gymReviewComment = "",
                isGymReviewSubmitting = false,
                reviewErrorMessage = null
            )
        }
    }

    fun onTrainerReviewRatingChanged(rating: Int) {
        _uiState.update { it.copy(trainerReviewRating = rating.coerceIn(0, 5), reviewErrorMessage = null) }
    }

    fun onTrainerReviewCommentChanged(comment: String) {
        _uiState.update { it.copy(trainerReviewComment = comment.take(255), reviewErrorMessage = null) }
    }

    fun onShouldLeaveGymReviewChanged(value: Boolean) {
        _uiState.update { it.copy(shouldLeaveGymReview = value, reviewErrorMessage = null) }
    }

    fun onGymReviewRatingChanged(rating: Int) {
        _uiState.update { it.copy(gymReviewRating = rating.coerceIn(0, 5), reviewErrorMessage = null) }
    }

    fun onGymReviewCommentChanged(comment: String) {
        _uiState.update { it.copy(gymReviewComment = comment.take(255), reviewErrorMessage = null) }
    }

    fun submitTrainerReview() {
        val state = _uiState.value
        val booking = state.reviewBooking ?: return
        if (state.isTrainerReviewSubmitting) return
        if (state.trainerReviewRating !in 1..5) return

        viewModelScope.launch {
            _uiState.update { it.copy(isTrainerReviewSubmitting = true, reviewErrorMessage = null) }
            runCatching {
                trainerReviewsRepository.createTrainerReview(
                    trainerId = booking.trainerId,
                    rating = state.trainerReviewRating,
                    comment = state.trainerReviewComment.trim().ifBlank { null }
                )
            }.onSuccess {
                if (state.shouldLeaveGymReview) {
                    _uiState.update {
                        it.copy(
                            isTrainerReviewSubmitting = false,
                            isTrainerReviewFormOpened = false,
                            isGymReviewFormOpened = true,
                            gymReviewRating = 0,
                            gymReviewComment = "",
                            reviewErrorMessage = null
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            isTrainerReviewSubmitting = false,
                            isTrainerReviewFormOpened = false,
                            isStatisticsTrainingsOpened = true,
                            reviewBooking = null,
                            trainerReviewRating = 0,
                            trainerReviewComment = "",
                            shouldLeaveGymReview = false,
                            reviewErrorMessage = null
                        )
                    }
                    loadBookings()
                }
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(
                        isTrainerReviewSubmitting = false,
                        reviewErrorMessage = throwable.message ?: "Не удалось отправить отзыв тренеру."
                    )
                }
            }
        }
    }

    fun submitGymReview() {
        val state = _uiState.value
        val booking = state.reviewBooking ?: return
        if (state.isGymReviewSubmitting) return
        if (state.gymReviewRating !in 1..5) return

        viewModelScope.launch {
            _uiState.update { it.copy(isGymReviewSubmitting = true, reviewErrorMessage = null) }
            runCatching {
                gymReviewsRepository.createGymReview(
                    gymId = booking.gymId,
                    rating = state.gymReviewRating,
                    comment = state.gymReviewComment.trim().ifBlank { null }
                )
            }.onSuccess {
                _uiState.update {
                    it.copy(
                        isGymReviewSubmitting = false,
                        isGymReviewFormOpened = false,
                        isStatisticsTrainingsOpened = true,
                        reviewBooking = null,
                        shouldLeaveGymReview = false,
                        gymReviewRating = 0,
                        gymReviewComment = "",
                        reviewErrorMessage = null
                    )
                }
                loadBookings()
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(
                        isGymReviewSubmitting = false,
                        reviewErrorMessage = throwable.message ?: "Не удалось отправить отзыв залу."
                    )
                }
            }
        }
    }

    fun onWeightInputChanged(value: String) {
        _uiState.update {
            it.copy(
                weightInput = value,
                progressSaveErrorMessage = null,
                progressSaveInfoMessage = null
            )
        }
    }

    fun onHeightInputChanged(value: String) {
        _uiState.update {
            it.copy(
                heightInput = value,
                progressSaveErrorMessage = null,
                progressSaveInfoMessage = null
            )
        }
    }

    fun calculateAndSaveBmi() {
        if (_uiState.value.isProgressSaving) return

        val weight = _uiState.value.weightInput.toPositiveNumberOrNull()
        val heightCm = _uiState.value.heightInput.toPositiveNumberOrNull()

        if (weight == null || heightCm == null) {
            _uiState.update {
                it.copy(
                    progressSaveErrorMessage = "Введите корректные вес и рост.",
                    progressSaveInfoMessage = null
                )
            }
            return
        }

        val heightMeters = heightCm / 100.0
        if (heightMeters <= 0.0) {
            _uiState.update {
                it.copy(
                    progressSaveErrorMessage = "Рост должен быть больше 0.",
                    progressSaveInfoMessage = null
                )
            }
            return
        }

        val bmi = round((weight / heightMeters.pow(2)) * 100.0) / 100.0

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isProgressSaving = true,
                    bmiValue = bmi,
                    progressSaveErrorMessage = null,
                    progressSaveInfoMessage = null
                )
            }

            runCatching {
                clientServicesRepository.createMyProgress(
                    weight = weight,
                    height = heightCm,
                    bmi = bmi
                )
            }.onSuccess { progress ->
                _uiState.update {
                    it.copy(
                        isProgressSaving = false,
                        bmiValue = progress.bmi,
                        progressSaveInfoMessage = "Данные сохранены.",
                        progressHistory = (it.progressHistory + progress)
                            .distinctBy { item -> item.id }
                            .sortedBy { item -> item.recordedAt }
                    )
                }
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(
                        isProgressSaving = false,
                        progressSaveErrorMessage =
                            throwable.message ?: "Не удалось сохранить прогресс веса."
                    )
                }
            }
        }
    }

    fun refreshProgressHistory() {
        loadProgressHistory()
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

    fun onEmailChanged(value: String) {
        _uiState.update { it.copy(email = value, errorMessage = null, infoMessage = null) }
    }

    fun onPushEnabledChanged(value: Boolean) {
        _uiState.update { it.copy(isPushEnabled = value) }
    }

    fun onMarketplaceSearchQueryChanged(value: String) {
        _uiState.update { it.copy(marketplaceSearchQuery = value) }
    }

    fun onMarketplaceCityFilterChanged(value: String) {
        _uiState.update { it.copy(marketplaceCityFilter = value) }
    }

    fun onMarketplaceRatingFilterChanged(value: Int?) {
        _uiState.update { it.copy(marketplaceRatingFilter = value) }
    }

    fun openGymDetails(gym: Gym) {
        _uiState.update {
            it.copy(
                selectedGym = gym,
                isGymPricesOpened = false,
                isGymReviewsOpened = false,
                isGymTrainersOpened = false,
                selectedTrainer = null,
                isTrainerReviewsOpened = false,
                gymPricesTab = GymPricesTab.Memberships,
                gymReviewsErrorMessage = null,
                gymTrainersErrorMessage = null,
                trainerReviewsErrorMessage = null
            )
        }
    }

    fun closeGymDetails() {
        _uiState.update {
            it.copy(
                selectedGym = null,
                isGymPricesOpened = false,
                isGymReviewsOpened = false,
                isGymTrainersOpened = false,
                selectedTrainer = null,
                isTrainerReviewsOpened = false
            )
        }
    }

    fun openGymPrices() {
        _uiState.update {
            if (it.selectedGym == null) {
                it
            } else {
                it.copy(
                    isGymPricesOpened = true,
                    isGymReviewsOpened = false,
                    isGymTrainersOpened = false,
                    selectedTrainer = null,
                    isTrainerReviewsOpened = false
                )
            }
        }
    }

    fun closeGymPrices() {
        _uiState.update { it.copy(isGymPricesOpened = false) }
    }

    fun openGymReviews() {
        val gymId = _uiState.value.selectedGym?.id ?: return
        _uiState.update {
            it.copy(
                isGymReviewsOpened = true,
                isGymPricesOpened = false,
                isGymTrainersOpened = false,
                selectedTrainer = null,
                isTrainerReviewsOpened = false
            )
        }
        if (_uiState.value.gymReviewsForGymId != gymId || _uiState.value.gymReviews.isEmpty()) {
            loadGymReviews(gymId)
        }
    }

    fun closeGymReviews() {
        _uiState.update { it.copy(isGymReviewsOpened = false) }
    }

    fun openGymTrainers() {
        val gymId = _uiState.value.selectedGym?.id ?: return
        _uiState.update {
            it.copy(
                isGymTrainersOpened = true,
                isGymPricesOpened = false,
                isGymReviewsOpened = false,
                selectedTrainer = null,
                isTrainerReviewsOpened = false
            )
        }
        if (_uiState.value.gymTrainersForGymId != gymId || _uiState.value.gymTrainers.isEmpty()) {
            loadGymTrainers(gymId)
        }
    }

    fun closeGymTrainers() {
        _uiState.update {
            it.copy(
                isGymTrainersOpened = false,
                selectedTrainer = null,
                isTrainerReviewsOpened = false
            )
        }
    }

    fun openTrainerDetails(trainer: GymTrainer) {
        _uiState.update {
            it.copy(
                selectedTrainer = trainer,
                isTrainerReviewsOpened = false,
                trainerReviewsErrorMessage = null
            )
        }
    }

    fun closeTrainerDetails() {
        _uiState.update {
            it.copy(
                selectedTrainer = null,
                isTrainerReviewsOpened = false
            )
        }
    }

    fun openTrainerReviews() {
        val trainerId = _uiState.value.selectedTrainer?.id ?: return
        _uiState.update {
            it.copy(
                isTrainerReviewsOpened = true
            )
        }
        if (
            _uiState.value.trainerReviewsForTrainerId != trainerId ||
            _uiState.value.trainerReviews.isEmpty()
        ) {
            loadTrainerReviews(trainerId)
        }
    }

    fun closeTrainerReviews() {
        _uiState.update { it.copy(isTrainerReviewsOpened = false) }
    }

    fun selectGymPricesTab(tab: GymPricesTab) {
        _uiState.update { it.copy(gymPricesTab = tab) }
    }

    fun searchGyms() {
        loadGyms()
    }

    fun refreshActiveServices() {
        loadActiveServices()
    }

    fun refreshMemberships() {
        loadMemberships()
    }

    fun activateMembership(membershipId: String) {
        if (_uiState.value.activatingMembershipId != null) return
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    activatingMembershipId = membershipId,
                    membershipsErrorMessage = null
                )
            }
            runCatching { clientServicesRepository.activateMembership(membershipId) }
                .onSuccess { updatedMembership ->
                    _uiState.update { state ->
                        state.copy(
                            activatingMembershipId = null,
                            memberships = state.memberships.map { membership ->
                                if (membership.id == updatedMembership.id) updatedMembership else membership
                            }
                        )
                    }
                    loadMemberships()
                    loadActiveServices()
                }
                .onFailure { throwable ->
                    _uiState.update {
                        it.copy(
                            activatingMembershipId = null,
                            membershipsErrorMessage =
                                throwable.message ?: "Не удалось активировать абонемент."
                        )
                    }
                }
        }
    }

    fun refreshPackages() {
        loadPackages()
    }

    fun activatePackage(packageId: String) {
        if (_uiState.value.activatingPackageId != null) return
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    activatingPackageId = packageId,
                    packagesErrorMessage = null
                )
            }
            runCatching { clientServicesRepository.activatePackage(packageId) }
                .onSuccess { updatedPackage ->
                    _uiState.update { state ->
                        state.copy(
                            activatingPackageId = null,
                            packages = state.packages.map { packageItem ->
                                if (packageItem.id == updatedPackage.id) updatedPackage else packageItem
                            }
                        )
                    }
                    loadPackages()
                    loadActiveServices()
                }
                .onFailure { throwable ->
                    _uiState.update {
                        it.copy(
                            activatingPackageId = null,
                            packagesErrorMessage = throwable.message ?: "Не удалось активировать пакет."
                        )
                    }
                }
        }
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

    private fun loadProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, infoMessage = null) }
            runCatching { profileRepository.getMyProfile() }
                .onSuccess { profile ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            userId = profile.id,
                            firstName = profile.firstName,
                            lastName = profile.lastName,
                            patronymic = profile.patronymic.orEmpty(),
                            birthDate = profile.birthDate.orEmpty(),
                            email = profile.email,
                            phone = profile.phone.orEmpty(),
                            description = profile.description.orEmpty()
                        )
                    }
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

    private fun loadActiveServices() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isActiveServicesLoading = true,
                    activeServicesErrorMessage = null
                )
            }
            runCatching { clientServicesRepository.getMyActiveServices() }
                .onSuccess { activeServices ->
                    _uiState.update {
                        it.copy(
                            isActiveServicesLoading = false,
                            activeMembership = activeServices.activeMembership,
                            activePackage = activeServices.activePackage
                        )
                    }
                }
                .onFailure { throwable ->
                    _uiState.update {
                        it.copy(
                            isActiveServicesLoading = false,
                            activeServicesErrorMessage =
                                throwable.message ?: "Failed to load active services."
                        )
                    }
                }
        }
    }

    private fun loadMemberships() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isMembershipsLoading = true,
                    membershipsErrorMessage = null
                )
            }
            runCatching { clientServicesRepository.getMyMemberships() }
                .onSuccess { memberships ->
                    val sortedMemberships = memberships.sortedBy { membership ->
                        when (membership.status) {
                            ClientMembershipStatus.ACTIVE -> 0
                            ClientMembershipStatus.PURCHASED -> 1
                            ClientMembershipStatus.EXPIRED -> 2
                        }
                    }
                    _uiState.update {
                        it.copy(
                            isMembershipsLoading = false,
                            memberships = sortedMemberships
                        )
                    }
                }
                .onFailure { throwable ->
                    _uiState.update {
                        it.copy(
                            isMembershipsLoading = false,
                            membershipsErrorMessage =
                                throwable.message ?: "Не удалось загрузить абонементы."
                        )
                    }
                }
        }
    }

    private fun loadPackages() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isPackagesLoading = true,
                    packagesErrorMessage = null
                )
            }
            runCatching { clientServicesRepository.getMyPackages() }
                .onSuccess { packages ->
                    val sortedPackages = packages.sortedBy { packageItem ->
                        when (packageItem.status) {
                            ClientTrainerPackageStatus.ACTIVE -> 0
                            ClientTrainerPackageStatus.PURCHASED -> 1
                            ClientTrainerPackageStatus.FINISHED -> 2
                        }
                    }
                    _uiState.update {
                        it.copy(
                            isPackagesLoading = false,
                            packages = sortedPackages
                        )
                    }
                }
                .onFailure { throwable ->
                    _uiState.update {
                        it.copy(
                            isPackagesLoading = false,
                            packagesErrorMessage = throwable.message ?: "Не удалось загрузить пакеты."
                        )
                    }
                }
        }
    }

    private fun loadProgressHistory() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isProgressLoading = true,
                    progressLoadErrorMessage = null
                )
            }
            runCatching { clientServicesRepository.getMyProgress() }
                .onSuccess { progress ->
                    _uiState.update {
                        it.copy(
                            isProgressLoading = false,
                            progressHistory = progress.sortedBy { item -> item.recordedAt }
                        )
                    }
                }
                .onFailure { throwable ->
                    _uiState.update {
                        it.copy(
                            isProgressLoading = false,
                            progressLoadErrorMessage =
                                throwable.message ?: "Не удалось загрузить динамику веса."
                        )
                    }
                }
        }
    }

    fun openStatisticsPackageBooking(packageId: String) {
        val packageItem = _uiState.value.packages.firstOrNull { it.id == packageId } ?: return
        if (packageItem.status != ClientTrainerPackageStatus.ACTIVE) return
        if (packageItem.sessionsLeft <= 0) return

        _uiState.update {
            it.copy(
                isStatisticsPackageBookingOpened = true,
                isStatisticsPackagesOpened = false,
                isStatisticsMembershipsOpened = false,
                isStatisticsWeightOpened = false,
                isStatisticsWeightDynamicsOpened = false,
                isStatisticsTrainingsOpened = false,
                isTrainerReviewFormOpened = false,
                isGymReviewFormOpened = false,
                bookingPackage = packageItem,
                bookingSelectedDate = LocalDate.now(),
                bookingAvailableSlots = emptyList(),
                selectedBookingSlots = emptyList(),
                isBookingSlotsLoading = false,
                isBookingSubmitting = false,
                bookingSlotsErrorMessage = null,
                bookingInfoMessage = null
            )
        }

        loadSlotsForBookingDate()
    }

    fun closeStatisticsPackageBooking() {
        _uiState.update {
            it.copy(
                isStatisticsPackageBookingOpened = false,
                isStatisticsPackagesOpened = true,
                bookingPackage = null,
                bookingAvailableSlots = emptyList(),
                selectedBookingSlots = emptyList(),
                isBookingSlotsLoading = false,
                isBookingSubmitting = false,
                bookingSlotsErrorMessage = null,
                bookingInfoMessage = null
            )
        }
    }

    fun onBookingDateSelected(date: LocalDate) {
        if (_uiState.value.bookingSelectedDate == date) return
        _uiState.update {
            it.copy(
                bookingSelectedDate = date,
                bookingSlotsErrorMessage = null,
                bookingInfoMessage = null
            )
        }
        loadSlotsForBookingDate()
    }

    fun retryLoadBookingSlots() {
        loadSlotsForBookingDate()
    }

    fun toggleBookingSlot(slot: TrainerSlot) {
        val slotId = slot.id ?: return
        val isBookable = slot.bookingId == null || slot.bookingStatus == "CANCELLED"
        if (!isBookable) return

        val state = _uiState.value
        val packageItem = state.bookingPackage ?: return

        val existingIndex = state.selectedBookingSlots.indexOfFirst { it.slotId == slotId }
        if (existingIndex >= 0) {
            _uiState.update {
                it.copy(
                    selectedBookingSlots = it.selectedBookingSlots.filterNot { selected ->
                        selected.slotId == slotId
                    },
                    bookingInfoMessage = null
                )
            }
            return
        }

        if (state.selectedBookingSlots.size >= packageItem.sessionsLeft) {
            _uiState.update {
                it.copy(bookingInfoMessage = "Нельзя выбрать больше занятий, чем осталось в пакете.")
            }
            return
        }

        val bookingDate = runCatching { LocalDate.parse(slot.startTime.take(10)) }
            .getOrDefault(state.bookingSelectedDate)

        _uiState.update {
            it.copy(
                selectedBookingSlots = (it.selectedBookingSlots + PackageBookingSelection(
                    slotId = slotId,
                    date = bookingDate,
                    startTime = slot.startTime,
                    endTime = slot.endTime
                )).sortedWith(
                    compareBy<PackageBookingSelection> { selected -> selected.date }
                        .thenBy { selected -> selected.startTime }
                ),
                bookingInfoMessage = null
            )
        }
    }

    fun removeSelectedBookingSlot(slotId: String) {
        _uiState.update {
            it.copy(
                selectedBookingSlots = it.selectedBookingSlots.filterNot { selected ->
                    selected.slotId == slotId
                },
                bookingInfoMessage = null
            )
        }
    }

    fun submitBulkBooking() {
        val state = _uiState.value
        val packageItem = state.bookingPackage ?: return
        if (state.isBookingSubmitting) return
        if (state.selectedBookingSlots.isEmpty()) {
            _uiState.update {
                it.copy(bookingSlotsErrorMessage = "Выберите хотя бы один слот для записи.")
            }
            return
        }
        if (state.selectedBookingSlots.size > packageItem.sessionsLeft) {
            _uiState.update {
                it.copy(
                    bookingSlotsErrorMessage = "Выбрано больше слотов, чем осталось занятий в пакете."
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isBookingSubmitting = true,
                    bookingSlotsErrorMessage = null,
                    bookingInfoMessage = null
                )
            }

            runCatching {
                clientServicesRepository.createMyBulkBookings(
                    trainerSlotIds = state.selectedBookingSlots.map { it.slotId }
                )
            }.onSuccess {
                _uiState.update {
                    it.copy(
                        isBookingSubmitting = false,
                        isStatisticsPackageBookingOpened = false,
                        isStatisticsPackagesOpened = true,
                        bookingPackage = null,
                        bookingAvailableSlots = emptyList(),
                        selectedBookingSlots = emptyList(),
                        bookingInfoMessage = "Запись успешно создана."
                    )
                }
                loadPackages()
                loadActiveServices()
                loadBookings()
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(
                        isBookingSubmitting = false,
                        bookingSlotsErrorMessage = throwable.message ?: "Не удалось записаться на занятия."
                    )
                }
            }
        }
    }

    private fun loadSlotsForBookingDate() {
        val state = _uiState.value
        val packageItem = state.bookingPackage ?: return
        val trainerId = packageItem.trainerId
        val selectedDate = state.bookingSelectedDate

        if (selectedDate.isBefore(LocalDate.now())) {
            _uiState.update {
                it.copy(
                    isBookingSlotsLoading = false,
                    bookingAvailableSlots = emptyList(),
                    bookingSlotsErrorMessage = null,
                    bookingInfoMessage = "Для прошедших дат запись недоступна."
                )
            }
            return
        }

        if (trainerId.isNullOrBlank()) {
            _uiState.update {
                it.copy(
                    bookingAvailableSlots = emptyList(),
                    bookingSlotsErrorMessage = "Не удалось определить тренера для выбранного пакета."
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isBookingSlotsLoading = true,
                    bookingSlotsErrorMessage = null
                )
            }

            runCatching {
                trainerSlotsRepository.getTrainerSlotsByDate(
                    trainerId = trainerId,
                    date = selectedDate.toString()
                )
            }.onSuccess { slots ->
                val availableSlots = slots.filter { slot ->
                    slot.id != null && (slot.bookingId == null || slot.bookingStatus == "CANCELLED")
                }.sortedBy { slot -> slot.startTime }

                _uiState.update {
                    it.copy(
                        isBookingSlotsLoading = false,
                        bookingAvailableSlots = availableSlots,
                        bookingSlotsErrorMessage = null
                    )
                }
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(
                        isBookingSlotsLoading = false,
                        bookingAvailableSlots = emptyList(),
                        bookingSlotsErrorMessage = throwable.message ?: "Не удалось загрузить слоты тренера."
                    )
                }
            }
        }
    }

    private fun loadBookings() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isBookingsLoading = true,
                    bookingsErrorMessage = null
                )
            }
            runCatching { clientServicesRepository.getMyBookings() }
                .onSuccess { bookings ->
                    _uiState.update {
                        it.copy(
                            isBookingsLoading = false,
                            upcomingBookings = bookings.upcoming.sortedBy { item -> item.toBookingDateTime() },
                            pastBookings = bookings.past.sortedByDescending { item -> item.toBookingDateTime() }
                        )
                    }
                }
                .onFailure { throwable ->
                    _uiState.update {
                        it.copy(
                            isBookingsLoading = false,
                            bookingsErrorMessage = throwable.message ?: "Не удалось загрузить тренировки."
                        )
                    }
                }
        }
    }

    private fun String.toPositiveNumberOrNull(): Double? {
        return trim()
            .replace(',', '.')
            .toDoubleOrNull()
            ?.takeIf { it > 0.0 }
    }

    private fun ClientBooking.toBookingDateTime(): LocalDateTime {
        val date = runCatching { LocalDate.parse(this.date, DateTimeFormatter.ISO_LOCAL_DATE) }
            .getOrDefault(LocalDate.MIN)
        val time = runCatching { LocalTime.parse(this.startTime, DateTimeFormatter.ISO_LOCAL_TIME) }
            .getOrElse {
                val normalized = this.startTime.take(5)
                runCatching { LocalTime.parse(normalized, DateTimeFormatter.ofPattern("HH:mm")) }
                    .getOrDefault(LocalTime.MIN)
            }
        return LocalDateTime.of(date, time)
    }

    private fun loadGyms() {
        val state = _uiState.value
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isGymsLoading = true,
                    gymsErrorMessage = null
                )
            }
            runCatching {
                gymRepository.getGyms(
                    name = state.marketplaceSearchQuery.trim().ifBlank { null },
                    city = state.marketplaceCityFilter.trim().ifBlank { null },
                    rating = state.marketplaceRatingFilter
                )
            }.onSuccess { gyms ->
                _uiState.update {
                    it.copy(
                        isGymsLoading = false,
                        gyms = gyms,
                        selectedGym = it.selectedGym?.let { selected ->
                            gyms.firstOrNull { gym -> gym.id == selected.id }
                        }
                    )
                }
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(
                        isGymsLoading = false,
                        gymsErrorMessage = throwable.message ?: "Failed to load gyms."
                    )
                }
            }
        }
    }

    private fun loadGymReviews(gymId: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isGymReviewsLoading = true,
                    gymReviewsErrorMessage = null
                )
            }
            runCatching { gymReviewsRepository.getGymReviews(gymId) }
                .onSuccess { reviews ->
                    val average = reviews
                        .map { it.rating.toDouble() }
                        .takeIf { it.isNotEmpty() }
                        ?.average()
                    _uiState.update {
                        it.copy(
                            isGymReviewsLoading = false,
                            gymReviews = reviews,
                            gymReviewsForGymId = gymId,
                            gymReviewsAverageRating = average
                        )
                    }
                }
                .onFailure { throwable ->
                    _uiState.update {
                        it.copy(
                            isGymReviewsLoading = false,
                            gymReviewsErrorMessage = throwable.message ?: "Failed to load gym reviews."
                        )
                    }
                }
        }
    }

    private fun loadGymTrainers(gymId: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isGymTrainersLoading = true,
                    gymTrainersErrorMessage = null
                )
            }
            runCatching { gymTrainersRepository.getGymTrainers(gymId) }
                .onSuccess { trainers ->
                    _uiState.update {
                        it.copy(
                            isGymTrainersLoading = false,
                            gymTrainers = trainers,
                            gymTrainersForGymId = gymId
                        )
                    }
                }
                .onFailure { throwable ->
                    _uiState.update {
                        it.copy(
                            isGymTrainersLoading = false,
                            gymTrainersErrorMessage = throwable.message ?: "Failed to load trainers."
                        )
                    }
                }
        }
    }

    private fun loadTrainerReviews(trainerId: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isTrainerReviewsLoading = true,
                    trainerReviewsErrorMessage = null
                )
            }
            runCatching { trainerReviewsRepository.getTrainerReviews(trainerId) }
                .onSuccess { reviews ->
                    val average = reviews
                        .map { it.rating.toDouble() }
                        .takeIf { it.isNotEmpty() }
                        ?.average()
                    _uiState.update {
                        it.copy(
                            isTrainerReviewsLoading = false,
                            trainerReviews = reviews,
                            trainerReviewsForTrainerId = trainerId,
                            trainerReviewsAverageRating = average
                        )
                    }
                }
                .onFailure { throwable ->
                    _uiState.update {
                        it.copy(
                            isTrainerReviewsLoading = false,
                            trainerReviewsErrorMessage = throwable.message ?: "Failed to load trainer reviews."
                        )
                    }
                }
        }
    }

    companion object {
        fun factory(
            profileRepository: ProfileRepository,
            clientServicesRepository: ClientServicesRepository,
            gymRepository: GymRepository,
            gymReviewsRepository: GymReviewsRepository,
            gymTrainersRepository: GymTrainersRepository,
            trainerReviewsRepository: TrainerReviewsRepository,
            trainerSlotsRepository: TrainerSlotsRepository
        ): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return ClientHomeViewModel(
                        profileRepository = profileRepository,
                        clientServicesRepository = clientServicesRepository,
                        gymRepository = gymRepository,
                        gymReviewsRepository = gymReviewsRepository,
                        gymTrainersRepository = gymTrainersRepository,
                        trainerReviewsRepository = trainerReviewsRepository,
                        trainerSlotsRepository = trainerSlotsRepository
                    ) as T
                }
            }
    }
}
