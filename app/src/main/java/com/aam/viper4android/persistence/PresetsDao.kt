package com.aam.viper4android.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aam.viper4android.persistence.model.PersistedPreset

@Dao
interface PresetsDao {
    // Get a preset by device id
    @Query("SELECT * FROM presets WHERE route_id = :routeId")
    suspend fun get(routeId: String): PersistedPreset?

    // Insert preset
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(preset: PersistedPreset)
}