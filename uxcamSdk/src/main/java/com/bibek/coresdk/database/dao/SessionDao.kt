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
    /**
     * Inserts or updates a session in the database.
     *
     * @param session The session to be inserted or updated.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: UxSession?)

    /**
     * Inserts or updates an event and returns the inserted row ID.
     *
     * @param event The event to be inserted or updated.
     * @return The row ID of the inserted event.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: UxEvent?): Long

    /**
     * Retrieves all sessions from the database.
     *
     * @return A list of all sessions.
     */
    @Query("SELECT * FROM UxSession")
    suspend fun getAllSessions(): List<UxSession>

    /**
     * Retrieves all events associated with a specific session.
     *
     * @param sessionId The ID of the session whose events are to be fetched.
     * @return A list of events for the specified session.
     */
    @Query("SELECT * FROM UxEvent WHERE sessionId = :sessionId")
    suspend fun getEventsForSession(sessionId: String): List<UxEvent>

    /**
     * Deletes a session and its related events.
     *
     * @param sessionId The ID of the session to be deleted.
     */
    @Transaction
    @Query("DELETE FROM UxSession WHERE id = :sessionId")
    suspend fun deleteSession(sessionId: String)

    /**
     * Deletes events associated with a specific session.
     *
     * @param sessionId The ID of the session whose events are to be deleted.
     */
    @Query("DELETE FROM UxEvent WHERE sessionId = :sessionId")
    suspend fun deleteEventsForSession(sessionId: String)
}
