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

    suspend fun endSession() {
        currentSession?.let {
            it.endTime = System.currentTimeMillis()
            sessionDao.insertSession(it)
            currentSession = null
        }
    }

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

    private fun generateSessionId(): String = UUID.randomUUID().toString()
}

