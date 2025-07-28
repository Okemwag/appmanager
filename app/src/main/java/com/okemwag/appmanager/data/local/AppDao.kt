package com.okemwag.appmanager.data.local

import androidx.room.*
import com.okemwag.appmanager.data.model.AppProfile
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface AppDao {
    @Query("SELECT * FROM app_profiles WHERE appPackageName = :packageName")
    fun getProfilesForApp(packageName: String): Flow<List<AppProfile>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: AppProfile)

    @Query("DELETE FROM app_profiles WHERE id = :profileId")
    suspend fun deleteProfile(profileId: String)

    @Query("UPDATE app_profiles SET lastUsed = :lastUsed WHERE id = :profileId")
    suspend fun updateLastUsed(profileId: String, lastUsed: LocalDateTime)
}
