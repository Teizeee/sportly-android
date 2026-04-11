package com.simple.sportly.ui.screen.client

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.simple.sportly.domain.model.Gym
import com.simple.sportly.domain.model.GymReview
import com.simple.sportly.domain.model.GymTrainer
import com.simple.sportly.domain.model.TrainerReview
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

enum class ClientTab {
    Activities,
    Notifications,
    Refresh,
    Profile
}

enum class GymPricesTab {
    Memberships,
    Packages
}

data class ClientHomeUiState(
    val selectedTab: ClientTab = ClientTab.Profile,
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val userId: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val patronymic: String = "",
    val email: String = "",
    val birthDate: String = "",
    val phone: String = "",
    val description: String = "",
    val isPushEnabled: Boolean = false,
    val marketplaceSearchQuery: String = "",
    val marketplaceCityFilter: String = "",
    val marketplaceRatingFilter: Int? = null,
    val gyms: List<Gym> = emptyList(),
    val selectedGym: Gym? = null,
    val isGymPricesOpened: Boolean = false,
    val isGymReviewsOpened: Boolean = false,
    val isGymTrainersOpened: Boolean = false,
    val gymPricesTab: GymPricesTab = GymPricesTab.Memberships,
    val gymReviews: List<GymReview> = emptyList(),
    val gymReviewsForGymId: String? = null,
    val gymReviewsAverageRating: Double? = null,
    val isGymReviewsLoading: Boolean = false,
    val gymReviewsErrorMessage: String? = null,
    val gymTrainers: List<GymTrainer> = emptyList(),
    val gymTrainersForGymId: String? = null,
    val selectedTrainer: GymTrainer? = null,
    val isTrainerReviewsOpened: Boolean = false,
    val trainerReviews: List<TrainerReview> = emptyList(),
    val trainerReviewsForTrainerId: String? = null,
    val trainerReviewsAverageRating: Double? = null,
    val isTrainerReviewsLoading: Boolean = false,
    val trainerReviewsErrorMessage: String? = null,
    val isGymTrainersLoading: Boolean = false,
    val gymTrainersErrorMessage: String? = null,
    val isGymsLoading: Boolean = false,
    val gymsErrorMessage: String? = null,
    val errorMessage: String? = null,
    val infoMessage: String? = null
)

class ClientHomeViewModel(
    private val profileRepository: ProfileRepository,
    private val gymRepository: GymRepository,
    private val gymReviewsRepository: GymReviewsRepository,
    private val gymTrainersRepository: GymTrainersRepository,
    private val trainerReviewsRepository: TrainerReviewsRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ClientHomeUiState())
    val uiState: StateFlow<ClientHomeUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    fun selectTab(tab: ClientTab) {
        _uiState.update { it.copy(selectedTab = tab) }
        if (tab == ClientTab.Activities && _uiState.value.gyms.isEmpty()) {
            loadGyms()
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
                        gymRepository = gymRepository,
                        gymReviewsRepository = gymReviewsRepository,
                        gymTrainersRepository = gymTrainersRepository,
                        trainerReviewsRepository = trainerReviewsRepository
                    ) as T
                }
            }
    }
}
