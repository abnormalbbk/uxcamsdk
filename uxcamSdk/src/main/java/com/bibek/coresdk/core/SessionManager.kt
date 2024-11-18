package com.bibek.coresdk.core

import com.bibek.coresdk.database.dao.SessionDao
import com.bibek.coresdk.database.entity.UxEvent
import com.bibek.coresdk.database.entity.UxSession
import com.bibek.coresdk.extensions.toJson
import com.bibek.coresdk.utils.UxLogger
import java.util.UUID

class SessionManager(private val sessionDao: SessionDao) {
    private val TAG: String = this::class::simpleName.name

    private var currentSession: UxSession? = null

    /**
     * Starts a new session if no session is currently running. If a session is already active,
     * an exception is thrown. This method creates a new `UxSession` with the provided user
     * information and session details, stores it in the `sessionDao`, and returns the session ID.
     *
     * @param email The email address of the user initiating the session.
     * @param name The name of the user initiating the session.
     * @param deviceId The unique identifier of the device on which the session is running.
     * @param packageName The name of the package associated with the session.
     *
     * @return The session ID of the newly created session, or `null` if no session was started.
     *
     * @throws IllegalStateException If a session is already running.
     */

    suspend fun startSession(
        email: String,
        name: String,
        deviceId: String,
        packageName: String
    ): String? {
        if (currentSession != null) throw IllegalStateException("Session Already Running")

        currentSession = UxSession(
            id = generateSessionId(),
            startTime = System.currentTimeMillis(),
            email = email,
            deviceId = deviceId,
            name = name,
            packageName = packageName
        )
        sessionDao.insertSession(currentSession)
        return currentSession?.id
    }

    /**
     * Ends the current session if one is active.
     */
    suspend fun endSession() {
        currentSession?.let {
            it.endTime = System.currentTimeMillis()
            sessionDao.insertSession(it)
            currentSession = null
        }
    }

    /**
     * Adds an event to the current session.
     *
     * @param name The name of the event to be added.
     * @param properties A map of additional properties associated with the event.
     *
     * @throws Exception If there is an error during event insertion into the database.
     */

    suspend fun addEvent(name: String, properties: Map<String, Any>) {
        if (currentSession == null) {
            UxLogger.e("XXXXX", "Unable to Add Session as current session is null")
        } else {
            val json = properties.toJson()
            UxLogger.d(TAG, "Adding Events to session  : ${currentSession!!.id}")
            val event = UxEvent(
                id = generateSessionId(),
                sessionId = currentSession!!.id,
                name = name,
                propertiesJson = json
            )
            UxLogger.d(TAG, "Event Detail  : $event")
            try {
                val id = sessionDao.insertEvent(event)
                UxLogger.d(TAG, "Inserted Event ID  : $id")
            } catch (e: Exception) {
                UxLogger.e(TAG, "Inserted Event Error  : ${e.printStackTrace()}")
            }


        }
    }

    /**
     * Generates a unique session ID using a randomly generated UUID.
     *
     * @return A string representing a unique session ID.
     */
    private fun generateSessionId(): String = UUID.randomUUID().toString()
}

