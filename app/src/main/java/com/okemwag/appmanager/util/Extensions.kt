package com.okemwag.appmanager.util

import com.okemwag.appmanager.domain.model.AppProfile as DomainProfile
import com.okemwag.appmanager.data.model.AppProfileEntity as EntityProfile

fun DomainProfile.toEntity(packageName: String): EntityProfile {
    return EntityProfile(
        id = this.id,
        appPackageName = packageName,
        profileName = this.name,
        profileIcon = this.icon,
        isActive = this.isActive,
        createdAt = this.createdAt,
        lastUsed = this.lastUsed
    )
}
