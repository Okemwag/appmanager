package com.okemwag.appmanager.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.okemwag.appmanager.domain.model.AppProfile
import com.okemwag.appmanager.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileSelectionUiState(
    val profiles: List<AppProfile> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isCreatingProfile: Boolean = false,
    val showCreateDialog: Boolean = false
)

@HiltViewModel
class ProfileSelectionViewModel @Inject constructor(
    private val getAppProfilesUseCase: GetAppProfilesUseCase,
    private val createProfileUseCase: CreateProfileUseCase,
    private val launchAppWithProfileUseCase: LaunchAppWithProfileUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileSelectionUiState())
    val uiState: StateFlow<ProfileSelectionUiState> = _uiState.asStateFlow()

    private lateinit var currentAppPackage: String

    fun initializeForApp(packageName: String) {
        currentAppPackage = packageName
        loadProfiles()
    }

    private fun loadProfiles() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            getAppProfilesUseCase(currentAppPackage)
                .catch { exception ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = exception.message ?: "Failed to load profiles"
                        )
                    }
                }
                .collect { profiles ->
                    _uiState.update {
                        it.copy(
                            profiles = profiles,
                            isLoading = false,
                            error = null
                        )
                    }
                }
        }
    }

    fun createProfile(profileName: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isCreatingProfile = true) }

            createProfileUseCase(currentAppPackage, profileName)
                .onSuccess {
                    _uiState.update {
                        it.copy(
                            isCreatingProfile = false,
                            showCreateDialog = false
                        )
                    }
                    // Profiles will be updated automatically through the flow
                }
                .onFailure { exception ->
                    _uiState.update {
                        it.copy(
                            isCreatingProfile = false,
                            error = exception.message ?: "Failed to create profile"
                        )
                    }
                }
        }
    }

    fun launchAppWithProfile(profileId: String) {
        viewModelScope.launch {
            launchAppWithProfileUseCase(currentAppPackage, profileId)
                .onFailure { exception ->
                    _uiState.update {
                        it.copy(error = exception.message ?: "Failed to launch app")
                    }
                }
        }
    }

    fun showCreateDialog() {
        _uiState.update { it.copy(showCreateDialog = true) }
    }

    fun hideCreateDialog() {
        _uiState.update { it.copy(showCreateDialog = false) }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
