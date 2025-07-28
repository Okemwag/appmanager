package com.okemwag.appmanager.domain.model

data class InstalledApp (
    val packageName: String,
    val appName: String,
    val icon: android.graphics.drawable.Drawable?,
    val versionName: String,
    val isSystemApp: Boolean,
    val isSupported: Boolean = false,
    val profiles: List<AppProfile> = emptyList()
)