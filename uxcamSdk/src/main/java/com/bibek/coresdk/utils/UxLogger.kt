package com.bibek.coresdk.utils

import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object UxLogger {

    // Define log levels for custom filtering
    enum class LogLevel { DEBUG, INFO, WARN, ERROR }

    // Set the minimum log level to control what logs get printed or saved
    var minLogLevel: LogLevel = LogLevel.DEBUG

    // Optionally, set the file for saving logs
    var logFile: File? = null

    // Format for log timestamps
    private val dateFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault())

    // Log function with filtering and optional file saving
    fun log(level: LogLevel, tag: String?, message: String, throwable: Throwable? = null) {
        val tagX = tag ?: "XXXX"
        if (true) {
            val formattedMessage = "${dateFormatter.format(Date())} [$level] $tag: $message"

            // Print to Logcat
            when (level) {
                LogLevel.DEBUG -> Log.d(tagX, message, throwable)
                LogLevel.INFO -> Log.i(tagX, message, throwable)
                LogLevel.WARN -> Log.w(tagX, message, throwable)
                LogLevel.ERROR -> Log.e(tagX, message, throwable)
            }

            // Save to file if specified
            logFile?.let { saveLogToFile(it, formattedMessage) }
        }
    }

    // Convenience methods for each log level
    fun d(tag: String? = null, message: String, throwable: Throwable? = null) =
        log(LogLevel.DEBUG, tag, message, throwable)

    fun i(tag: String? = null, message: String, throwable: Throwable? = null) =
        log(LogLevel.INFO, tag, message, throwable)

    fun w(tag: String? = null, message: String, throwable: Throwable? = null) =
        log(LogLevel.WARN, tag, message, throwable)

    fun e(tag: String? = null, message: String, throwable: Throwable? = null) =
        log(LogLevel.ERROR, tag, message, throwable)


    private fun saveLogToFile(file: File, message: String) {
        try {
            FileOutputStream(file, true).bufferedWriter().use { writer ->
                writer.appendLine(message)
            }
        } catch (e: Exception) {
            Log.e(this::class::simpleName.name, "Failed to save log to file", e)
        }
    }
}
