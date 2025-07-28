package com.okemwag.appmanager.data.repository

import android.content.Context
import android.content.pm.PackageManager
import com.okemwag.appmanager.data.local.AppDao
import com.okemwag.appmanager.domain.model.InstalledApp
import com.okemwag.appmanager.domain.model.AppProfile
import com.okemwag.appmanager.domain.repository.AppRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton
import dagger.hilt.android.qualifiers.ApplicationContext

@Singleton
class AppRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val appDao: AppDao
) : AppRepository {

    private val packageManager = context.packageManager

    override fun getInstalledApps(): Flow<List<InstalledApp>> = flow {
        val packages = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
        val apps = packages.map { appInfo ->
            InstalledApp(
                packageName = appInfo.packageName,
                appName = packageManager.getApplicationLabel(appInfo).toString(),
                icon = packageManager.getApplicationIcon(appInfo),
                versionName = try {
                    packageManager.getPackageInfo(appInfo.packageName, 0).versionName
                } catch (e: Exception) { "Unknown" },
                isSystemApp = (appInfo.flags and android.content.pm.ApplicationInfo.FLAG_SYSTEM) != 0,
                isSupported = getSupportedPackages().contains(appInfo.packageName)
            )
        }.filter { !it.isSystemApp }

        emit(apps)
    }

    override fun getSupportedApps(): Flow<List<InstalledApp>> = flow {
        getInstalledApps().collect { apps ->
            emit(apps.filter { it.isSupported })
        }
    }

    override fun getAppProfiles(packageName: String): Flow<List<AppProfile>> =
        appDao.getProfilesForApp(packageName)

    override suspend fun createProfile(packageName: String, profileName: String): Result<AppProfile> {
        return try {
            val profile = AppProfile(
                id = java.util.UUID.randomUUID().toString(),
                name = profileName,
                icon = null,
                isActive = false,
                createdAt = java.time.LocalDateTime.now(),
                lastUsed = null
            )

            appDao.insertProfile(profile.toEntity(packageName))
            Result.success(profile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteProfile(profileId: String): Result<Unit> {
        return try {
            appDao.deleteProfile(profileId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun launchAppWithProfile(packageName: String, profileId: String): Result<Unit> {
        return try {
            // This would implement the actual app launching logic
            // For demonstration, we'll just mark the profile as last used
            appDao.updateLastUsed(profileId, java.time.LocalDateTime.now())

            val launchIntent = packageManager.getLaunchIntentForPackage(packageName)
            launchIntent?.let {
                it.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(it)
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun getSupportedPackages(): Set<String> = setOf(
        "com.whatsapp",
        "com.facebook.katana",
        "com.instagram.android",
        "com.twitter.android",
        "com.telegram.messenger",
        "com.snapchat.android"
    )
}