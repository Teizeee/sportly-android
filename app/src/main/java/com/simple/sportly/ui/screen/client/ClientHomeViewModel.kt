package com.simple.sportly.ui.screen.client

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.simple.sportly.domain.model.ActiveMembership
import com.simple.sportly.domain.model.ActivePackage
import com.simple.sportly.domain.model.ClientMembership
import com.simple.sportly.domain.model.ClientMembershipStatus
import com.simple.sportly.domain.model.ClientTrainerPackage
import com.simple.sportly.domain.model.ClientTrainerPackageStatus
import com.simple.sportly.domain.model.Gym
import com.simple.sportly.domain.model.GymReview
import com.simple.sportly.domain.model.GymTrainer
import com.simple.sportly.domain.model.TrainerReview
import com.simple.sportly.domain.repository.ClientServicesRepository
import com.simple.sportly.domain.repository.GymRepository
import com.simple.sportly.domain.repository.GymReviewsRepository
import com.simple.sportly.domain.repository.GymTrainersRepository
import com.simple.sportly.domain.repository.ProfileRepository
import com.simple.sportly.domain.repository.TrainerReviewsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.pow
import kotlin.math.round

class ClientHomeViewModel(
    private val profileRepository: ProfileRepository,
    private val clientServicesRepository: ClientServicesRepository,
    private val gymRepository: GymRepository,
    private val gymReviewsRepository: GymReviewsRepository,
    private val gymTrainersRepository: GymTrainersRepository,
    private val trainerReviewsRepository: TrainerReviewsRepository
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
        _uiState.update { it.copy(isStatisticsMembershipsOpened = true) }
        if (_uiState.value.memberships.isEmpty() && !_uiState.value.isMembershipsLoading) {
            loadMemberships()
        }
    }

    fun closeStatisticsMemberships() {
        _uiState.update { it.copy(isStatisticsMembershipsOpened = false) }
    }

    fun openStatisticsPackages() {
        _uiState.update { it.copy(isStatisticsPackagesOpened = true) }
        if (_uiState.value.packages.isEmpty() && !_uiState.value.isPackagesLoading) {
            loadPackages()
        }
    }

    fun closeStatisticsPackages() {
        _uiState.update { it.copy(isStatisticsPackagesOpened = false) }
    }

    fun openStatisticsWeight() {
        _uiState.update {
            it.copy(
                isStatisticsWeightOpened = true,
                isStatisticsWeightDynamicsOpened = false,
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

    private fun String.toPositiveNumberOrNull(): Double? {
        return trim()
            .replace(',', '.')
            .toDoubleOrNull()
            ?.takeIf { it > 0.0 }
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
            trainerReviewsRepository: TrainerReviewsRepository
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
                        trainerReviewsRepository = trainerReviewsRepository
                    ) as T
                }
            }
    }
}
