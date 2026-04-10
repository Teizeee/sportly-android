package com.simple.sportly.ui.screen.client

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.simple.sportly.domain.repository.AuthRepository
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
    val errorMessage: String? = null,
    val infoMessage: String? = null
)

class ClientHomeViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ClientHomeUiState())
    val uiState: StateFlow<ClientHomeUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    fun selectTab(tab: ClientTab) {
        _uiState.update { it.copy(selectedTab = tab) }
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

    fun saveProfile() {
        val state = _uiState.value
        if (state.firstName.isBlank() || state.lastName.isBlank() || state.email.isBlank()) {
            _uiState.update { it.copy(errorMessage = "First name, last name and email are required.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, errorMessage = null, infoMessage = null) }
            runCatching {
                authRepository.updateMyProfile(
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
            runCatching { authRepository.getMyProfile() }
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

    companion object {
        fun factory(authRepository: AuthRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return ClientHomeViewModel(authRepository) as T
                }
            }
    }
}
