package com.bibek.coresdk.utils

import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * UxLogger is a utility for logging messages with different log levels (DEBUG, INFO, WARN, ERROR).
 * It supports filtering logs based on the minimum log level and saving logs to a file.
 */
object UxLogger {

    // Define log levels for custom filtering
    enum class LogLevel { DEBUG, INFO, WARN, ERROR }

    // Set the minimum log level to control what logs get printed or saved
    var minLogLevel: LogLevel = LogLevel.DEBUG

    // Optionally, set the file for saving logs
    var logFile: File? = null

    // Format for log timestamps
    private val dateFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault())

    /**
     * Logs a message to Logcat and optionally saves it to a file.
     *
     * @param level The log level (DEBUG, INFO, WARN, ERROR).
     * @param tag The tag associated with the log message.
     * @param message The log message.
     * @param throwable An optional throwable to log.
     */
    fun log(level: LogLevel, tag: String?, message: String, throwable: Throwable? = null) {
        val tagX = tag ?: "XXXX"

        if (level >= minLogLevel) {
            val formattedMessage = "${dateFormatter.format(Date())} [$level] $tag: $message"

            when (level) {
                LogLevel.DEBUG -> Log.d(tagX, message, throwable)
                LogLevel.INFO -> Log.i(tagX, message, throwable)
                LogLevel.WARN -> Log.w(tagX, message, throwable)
                LogLevel.ERROR -> Log.e(tagX, message, throwable)
            }

            logFile?.let { saveLogToFile(it, formattedMessage) }
        }
    }

    /**
     * Logs a DEBUG level message.
     *
     * @param tag The tag associated with the log message.
     * @param message The log message.
     * @param throwable An optional throwable to log.
     */
    fun d(tag: String? = null, message: String, throwable: Throwable? = null) =
        log(LogLevel.DEBUG, tag, message, throwable)

    /**
     * Logs an INFO level message.
     *
     * @param tag The tag associated with the log message.
     * @param message The log message.
     * @param throwable An optional throwable to log.
     */
    fun i(tag: String? = null, message: String, throwable: Throwable? = null) =
        log(LogLevel.INFO, tag, message, throwable)

    /**
     * Logs a WARN level message.
     *
     * @param tag The tag associated with the log message.
     * @param message The log message.
     * @param throwable An optional throwable to log.
     */
    fun w(tag: String? = null, message: String, throwable: Throwable? = null) =
        log(LogLevel.WARN, tag, message, throwable)

    /**
     * Logs an ERROR level message.
     *
     * @param tag The tag associated with the log message.
     * @param message The log message.
     * @param throwable An optional throwable to log.
     */
    fun e(tag: String? = null, message: String, throwable: Throwable? = null) =
        log(LogLevel.ERROR, tag, message, throwable)

    /**
     * Saves a log message to the specified file.
     *
     * @param file The file to save the log message to.
     * @param message The log message to save.
     */
    private fun saveLogToFile(file: File, message: String) {
        try {
            FileOutputStream(file, true).bufferedWriter().use { writer ->
                writer.appendLine(message)
            }
        } catch (e: Exception) {
            Log.e(this::class.simpleName, "Failed to save log to file", e)
        }
    }
}
