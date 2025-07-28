package com.okemwag.appmanager.domain.usecase

import com.okemwag.appmanager.domain.model.AppProfile
import com.okemwag.appmanager.domain.repository.AppRepository
import kotlinx.coroutines.flow.Flow

class GetAppProfilesUseCase(
    private val repository: AppRepository
) {
    operator fun invoke(packageName: String): Flow<List<AppProfile>> {
        return repository.getProfilesForApp(packageName)
    }
}
