package com.okemwag.appmanager.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "apps")
data class AppEntity(
    @PrimaryKey val packageName: String,
    val appName: String,
    val iconPath: String?,
    val isSupported: Boolean = false,
    val maxInstances: Int = 1,
    val isPremium: Boolean = false
)
