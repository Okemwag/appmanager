package com.okemwag.appmanager.domain.usecase

import com.okemwag.appmanager.domain.model.AppProfile
import com.okemwag.appmanager.domain.repository.AppRepository
import com.okemwag.appmanager.domain.util.AppLauncher

class LaunchAppWithProfileUseCase(
    private val appLauncher: AppLauncher,
    private val repository: AppRepository
) {
    suspend operator fun invoke(profile: AppProfile) {
        // Optionally update usage stats
        repository.markProfileAsUsed(profile)

        // Launch app with settings from profile
        appLauncher.launch(profile)
    }
}
