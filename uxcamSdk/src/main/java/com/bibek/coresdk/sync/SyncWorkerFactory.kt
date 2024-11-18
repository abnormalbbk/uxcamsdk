package com.bibek.coresdk.sync

import android.content.Context
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters

/**
 * A custom `WorkerFactory` for creating instances of `SyncWorker`.
 * This factory is used to instantiate `SyncWorker` with the provided context and parameters.
 */
class SyncWorkerFactory : WorkerFactory() {

    /**
     * Creates a worker instance based on the provided worker class name and parameters.
     *
     * @param appContext The application context.
     * @param workerClassName The name of the worker class to instantiate.
     * @param workerParameters The parameters required to create the worker.
     * @return A new instance of `SyncWorker` or `null` if the worker class name doesn't match.
     */
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
