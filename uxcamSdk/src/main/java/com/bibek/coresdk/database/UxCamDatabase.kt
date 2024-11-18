package com.bibek.coresdk.database

import android.content.Context
import androidx.room.Room
import com.bibek.coresdk.database.dao.SessionDao
import com.bibek.coresdk.database.entity.UxEvent
import com.bibek.coresdk.database.entity.UxSession

@androidx.room.Database(
    entities = [UxSession::class, UxEvent::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : androidx.room.RoomDatabase() {
    abstract fun sessionDao(): SessionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Returns the singleton instance of the `AppDatabase`. If the instance does not exist,
         * it creates a new database instance using Room.
         *
         * @param context The application context used to initialize the database.
         * @return The singleton instance of `AppDatabase`.
         */

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "uxcam_database"
                ).build().also { INSTANCE = it }
            }
        }
    }
}