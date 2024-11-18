package com.bibek.coresdk

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.work.Configuration
import androidx.work.WorkManager
import com.bibek.coresdk.sync.SyncHelper
import com.bibek.coresdk.sync.SyncWorkerFactory
import com.bibek.coresdk.utils.UxLogger
import com.google.firebase.firestore.FirebaseFirestore

/**
 * The custom `Application` class for initializing the UxCam application, setting up the WorkManager,
 * and scheduling log synchronization tasks.
 */
open class UxCamApplication : Application(), Configuration.Provider {

    /**
     * Provides the configuration for the WorkManager.
     * Sets a custom worker factory and specifies the minimum logging level for WorkManager.
     *
     * @return A `Configuration` object for initializing the WorkManager.
     */
    override fun getWorkManagerConfiguration(): Configuration {
        val syncWorkerFactory = SyncWorkerFactory()
        return Configuration.Builder()
            .setWorkerFactory(syncWorkerFactory)
            .setMinimumLoggingLevel(Log.DEBUG)
            .build()
    }

    /**
     * Initializes WorkManager and schedules a log syncing task to run after a delay.
     * This method is called when the application is created.
     */
    override fun onCreate() {
        super.onCreate()

        if (!WorkManager.isInitialized()) {
            WorkManager.initialize(this, getWorkManagerConfiguration())
        }

        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            UxLogger.d(UxCamApplication::class.simpleName, "onCreate: Syncing Logs")
            SyncHelper.scheduleSync(this)
        }, 5000)
    }
}

/**
 * Interface providing access to Firebase Firestore.
 */
interface FirebaseProvider {
    /**
     * Returns the instance of Firestore for interacting with the Firebase database.
     */
    val firestore: FirebaseFirestore
}
