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

open class UxCamApplication : Application(), Configuration.Provider {

    override fun getWorkManagerConfiguration(): Configuration {
        val syncWorkerFactory = SyncWorkerFactory()
        return Configuration.Builder()
            .setWorkerFactory(syncWorkerFactory)
            .setMinimumLoggingLevel(Log.DEBUG)
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        if (!WorkManager.isInitialized()) {
            WorkManager.initialize(this, getWorkManagerConfiguration());
        }

        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            UxLogger.d(UxCamApplication::class.simpleName, "onCreate: Syncing Logs")
            SyncHelper.scheduleSync(this)
        }, 5000)
    }
}

interface FirebaseProvider {
    val firestore: FirebaseFirestore
}