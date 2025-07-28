package com.okemwag.appmanager.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.okemwag.appmanager.domain.model.InstalledApp
import com.okemwag.appmanager.domain.usecase.GetSupportedAppsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AppListUiState(
    val apps: List<InstalledApp> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = "",
    val filteredApps: List<InstalledApp> = emptyList()
)

@HiltViewModel
class AppListViewModel @Inject constructor(
    private val getSupportedAppsUseCase: GetSupportedAppsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AppListUiState())
    val uiState: StateFlow<AppListUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    init {
        loadApps()
        observeSearchQuery()
    }

    private fun loadApps() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            getSupportedAppsUseCase()
                .catch { exception ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = exception.message ?: "Unknown error occurred"
                        )
                    }
                }
                .collect { apps ->
                    _uiState.update {
                        it.copy(
                            apps = apps,
                            isLoading = false,
                            error = null
                        )
                    }
                    filterApps()
                }
        }
    }

    private fun observeSearchQuery() {
        viewModelScope.launch {
            _searchQuery
                .debounce(300) // Debounce search input
                .collect { query ->
                    _uiState.update { it.copy(searchQuery = query) }
                    filterApps()
                }
        }
    }

    private fun filterApps() {
        val currentState = _uiState.value
        val filteredApps = if (currentState.searchQuery.isBlank()) {
            currentState.apps
        } else {
            currentState.apps.filter { app ->
                app.appName.contains(currentState.searchQuery, ignoreCase = true) ||
                        app.packageName.contains(currentState.searchQuery, ignoreCase = true)
            }
        }

        _uiState.update { it.copy(filteredApps = filteredApps) }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    fun refresh() {
        loadApps()
    }
}