package com.okemwag.appmanager.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_preferences")
data class UserPreferences(
    @PrimaryKey val id: Int = 1,
    val isPremiumUser: Boolean = false,
    val maxFreeInstances: Int = 3,
    val theme: String = "auto",
    val notificationsEnabled: Boolean = true
)