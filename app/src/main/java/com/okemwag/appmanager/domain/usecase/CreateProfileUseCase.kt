package com.okemwag.appmanager.domain.usecase

import com.okemwag.appmanager.domain.model.AppProfile
import com.okemwag.appmanager.domain.repository.AppRepository

class CreateProfileUseCase(
    private val repository: AppRepository
) {
    suspend operator fun invoke(profile: AppProfile) {
        repository.saveProfile(profile)
    }
}
