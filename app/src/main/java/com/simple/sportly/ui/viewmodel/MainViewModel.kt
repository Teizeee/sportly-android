package com.simple.sportly.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.simple.sportly.data.local.SessionStore
import com.simple.sportly.domain.model.UserRole
import com.simple.sportly.ui.navigation.AppDestination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class MainUiState(
    val isLoading: Boolean = true,
    val startDestination: String = AppDestination.Login.route
)

class MainViewModel(
    private val sessionStore: SessionStore
) : ViewModel() {
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val session = sessionStore.session.first()
            val destination = when (session?.role) {
                UserRole.CLIENT -> AppDestination.ClientHome.route
                UserRole.TRAINER -> AppDestination.TrainerHome.route
                null -> AppDestination.Login.route
            }
            _uiState.value = MainUiState(
                isLoading = false,
                startDestination = destination
            )
        }
    }

    companion object {
        fun factory(sessionStore: SessionStore): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return MainViewModel(sessionStore) as T
                }
            }
    }
}
