package com.bibek.coresdk.sync

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager

object SyncHelper {
    /**
     * Schedules a one-time sync operation using `WorkManager`.
     *
     * @param context The application context used to enqueue the sync task.
     */

    fun scheduleSync(context: Context) {
        val syncRequest = OneTimeWorkRequestBuilder<SyncWorker>().build()
        WorkManager.getInstance(context).enqueue(syncRequest)
    }
}