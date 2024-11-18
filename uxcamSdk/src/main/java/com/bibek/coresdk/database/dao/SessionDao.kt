package com.bibek.coresdk.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.bibek.coresdk.database.entity.UxEvent
import com.bibek.coresdk.database.entity.UxSession

@Dao
interface SessionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: UxSession?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: UxEvent?): Long

    @Query("SELECT * FROM UxSession")
    suspend fun getAllSessions(): List<UxSession>

    @Query("SELECT * FROM UxEvent WHERE sessionId = :sessionId")
    suspend fun getEventsForSession(sessionId: String): List<UxEvent>

    /**
     * Delete a session and optionally its related events.
     */
    @Transaction
    @Query("DELETE FROM UxSession WHERE id = :sessionId")
    suspend fun deleteSession(sessionId: String)

    /**
     * Delete events associated with a session.
     */
    @Query("DELETE FROM UxEvent WHERE sessionId = :sessionId")
    suspend fun deleteEventsForSession(sessionId: String)
}