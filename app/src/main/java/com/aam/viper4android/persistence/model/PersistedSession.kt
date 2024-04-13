package com.aam.viper4android.persistence.model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "sessions", primaryKeys = ["id"])
data class PersistedSession(
    @ColumnInfo(name = "package_name") val packageName: String,
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "boot_count") val bootCount: Int,
)
