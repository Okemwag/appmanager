package com.okemwag.appmanager.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.okemwag.appmanager.data.model.AppEntity
import com.okemwag.appmanager.data.model.AppProfile
import com.okemwag.appmanager.data.model.UserPreferences

@Database(
    entities = [AppEntity::class, AppProfile::class, UserPreferences::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao
}
