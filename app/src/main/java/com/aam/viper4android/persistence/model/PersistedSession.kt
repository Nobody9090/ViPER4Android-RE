package com.aam.viper4android.persistence.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "sessions",
    primaryKeys = ["id", "boot_count"])
data class PersistedSession(
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "package_name") val packageName: String?,
    @ColumnInfo(name = "boot_count") val bootCount: Int,
)
