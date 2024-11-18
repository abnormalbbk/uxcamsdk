package com.bibek.coresdk.sync

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.bibek.coresdk.FirebaseProvider
import com.bibek.coresdk.database.AppDatabase
import com.bibek.coresdk.database.entity.UxEvent
import com.bibek.coresdk.database.entity.UxSession
import com.bibek.coresdk.extensions.toMap
import com.bibek.coresdk.utils.UxLogger
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

val TAG = SyncWorker::class.java.simpleName

class SyncWorker(
    context: Context,
    workerParams: WorkerParameters,
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {

        try {
            val sessionDao = AppDatabase.getInstance(applicationContext).sessionDao()
            val firebaseProvider = applicationContext as? FirebaseProvider
                ?: throw IllegalStateException("Host application must implement FirebaseProvider")
            val fireStore = firebaseProvider.firestore
            val unsyncedSessions = sessionDao.getAllSessions()
            for (session in unsyncedSessions) {
                val events = sessionDao.getEventsForSession(session.id)
                val success = sendToServer(fireStore, session, events)

                if (success) {
                    UxLogger.d(
                        TAG,
                        "Session ${session.id} synced successfully. Deleting local data."
                    )
                    sessionDao.deleteEventsForSession(session.id)
                    sessionDao.deleteSession(session.id)
                } else {
                    UxLogger.e(TAG, "Failed to sync session ${session.id}. Retrying later.")
                    return@withContext Result.retry()
                }
            }
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            UxLogger.e(TAG, "Failed to do the sync work. Retrying later.")
            Result.retry()
        }
    }

    private suspend fun sendToServer(
        firestore: FirebaseFirestore?,
        session: UxSession,
        events: List<UxEvent>
    ): Boolean {
        return suspendCoroutine { continuation ->
            try {
                val firestoreSession = FireStoreSession(
                    id = session.id,
                    startTime = session.startTime,
                    endTime = session.endTime,
                    events = events.map {
                        FireStoreEvent(
                            name = it.name,
                            properties = it.propertiesJson.toMap()
                        )
                    },
                    email = session.email,
                    name = session.name,
                    packageName = session.packageName,
                    deviceId = session.deviceId
                )

                firestore?.collection("sessions")
                    ?.document(session.id)
                    ?.set(firestoreSession, SetOptions.merge())
                    ?.addOnSuccessListener {
                        UxLogger.d(message = "Uploading Sessions and Events to FireStore Success")
                        continuation.resume(true)
                    }
                    ?.addOnFailureListener { exception ->
                        UxLogger.e(
                            message = "Uploading Sessions Failed with exception :: ${exception.printStackTrace()}"
                        )
                        continuation.resume(false)
                    }
            } catch (e: Exception) {
                UxLogger.e(
                    message = "Send to server failed with exception :: ${e.printStackTrace()}"
                )
                continuation.resume(false)
            }
        }
    }
}

data class FireStoreEvent(
    val name: String,
    val properties: Map<String, Any>
)

data class FireStoreSession(
    val id: String,
    val startTime: Long,
    val endTime: Long?,
    val email: String,
    val name: String,
    val packageName: String,
    val deviceId: String,
    val events: List<FireStoreEvent>
)