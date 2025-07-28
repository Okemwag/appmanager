package com.okemwag.appmanager.domain.usecase

import com.okemwag.appmanager.domain.model.SupportedApp
import com.okemwag.appmanager.domain.repository.AppRepository
import kotlinx.coroutines.flow.Flow

class GetSupportedAppsUseCase(
    private val repository: AppRepository
) {
    operator fun invoke(): Flow<List<SupportedApp>> {
        return repository.getSupportedApps()
    }
}
