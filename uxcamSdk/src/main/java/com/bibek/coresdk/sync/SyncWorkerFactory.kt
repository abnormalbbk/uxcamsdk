package com.bibek.coresdk.sync

import android.content.Context
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters

class SyncWorkerFactory(
) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): SyncWorker? {
        return when (workerClassName) {
            SyncWorker::class.java.name -> SyncWorker(
                appContext,
                workerParameters,
            )

            else -> null
        }
    }
}
