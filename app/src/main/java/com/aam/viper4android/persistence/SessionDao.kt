package com.aam.viper4android.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aam.viper4android.persistence.model.PersistedSession

@Dao
interface SessionDao {
    // Get all sessions
    @Query("SELECT * FROM sessions")
    suspend fun getAll(): List<PersistedSession>

    // Insert session
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(session: PersistedSession)

    // Delete session by packageName and sessionId
    @Query("DELETE FROM sessions WHERE id = :sessionId")
    suspend fun delete(sessionId: Int)

    // Delete all sessions where boot_count is not equal to the current boot count
    @Query("DELETE FROM sessions WHERE boot_count != :bootCount")
    suspend fun deleteObsolete(bootCount: Int)
}