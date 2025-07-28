package com.okemwag.appmanager.domain.repository

import com.okemwag.appmanager.domain.model.InstalledApp
import com.okemwag.appmanager.domain.model.AppProfile
import kotlinx.coroutines.flow.Flow

interface AppRepository {
    fun getInstalledApps(): Flow<List<InstalledApp>>
    fun getSupportedApps(): Flow<List<InstalledApp>>
    fun getAppProfiles(packageName: String): Flow<List<AppProfile>>
    suspend fun createProfile(packageName: String, profileName: String): Result<AppProfile>
    suspend fun deleteProfile(profileId: String): Result<Unit>
    suspend fun launchAppWithProfile(packageName: String, profileId: String): Result<Unit>
}