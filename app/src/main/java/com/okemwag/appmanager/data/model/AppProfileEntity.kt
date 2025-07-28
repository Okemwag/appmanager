package com.okemwag.appmanager.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "app_profiles")
data class AppProfile(
    @PrimaryKey val id: String,
    val appPackageName: String,
    val profileName: String,
    val profileIcon: String?,
    val isActive: Boolean = false,
    val createdAt: LocalDateTime,
    val lastUsed: LocalDateTime?
)