package com.okemwag.appmanager.domain.model

data class AppProfile (
    val id: String,
    val name: String,
    val icon: String?,
    val isActive: Boolean,
    val createdAt: java.time.LocalDateTime,
    val lastUsed: java.time.LocalDateTime?
)