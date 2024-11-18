package com.bibek.coresdk.core

import android.content.Context
import androidx.navigation.NavController
import com.bibek.coresdk.database.AppDatabase
import com.bibek.coresdk.utils.DeviceUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * UxCam is a singleton object for managing user session data and event tracking in the application.
 * It allows configuration of user details, session management, and event tracking during the session.
 */
object UxCam {

    /**
     * Indicates whether UxCam has been configured.
     */
    var isConfigured = false
        private set

    /**
     * Indicates whether a session has been started.
     */
    var isSessionStarted = false
        private set

    /**
     * The package name of the app.
     */
    var packageName: String = ""
        private set

    /**
     * The email of the user.
     */
    var email: String = ""
        private set

    /**
     * The name of the user.
     */
    var name: String = ""
        private set

    /**
     * The device ID.
     */
    var deviceId: String = ""
        private set

    /**
     * Manages session-related operations.
     */
    var sessionManager: SessionManager? = null
        private set

    /**
     * Sets the package name for the session.
     *
     * @param packageName The package name of the app.
     * @return The current instance of UxCam for method chaining.
     */
    fun setIdentifier(packageName: String) = apply { this.packageName = packageName }

    /**
     * Sets the email for the session.
     *
     * @param email The email of the user.
     * @return The current instance of UxCam for method chaining.
     */
    fun setEmail(email: String) = apply { this.email = email }

    /**
     * Sets the name for the session.
     *
     * @param name The name of the user.
     * @return The current instance of UxCam for method chaining.
     */
    fun setName(name: String) = apply { this.name = name }

    /**
     * Configures UxCam by setting required values and initializing session management.
     *
     * @param context The application context.
     * @return The current instance of UxCam.
     * @throws IllegalArgumentException If required parameters are not set before building.
     */
    fun build(context: Context): UxCam {
        require(packageName.isNotBlank()) { "Package name must be set before building UxCam." }
        require(email.isNotBlank()) { "Email must be set before building UxCam." }
        require(name.isNotBlank()) { "Name must be set before building UxCam." }

        deviceId = DeviceUtil.getDeviceId(context)
        val database = AppDatabase.getInstance(context)
        sessionManager = SessionManager(database.sessionDao())
        isConfigured = true
        return this
    }

    /**
     * Checks if UxCam is configured.
     *
     * @throws IllegalStateException If UxCam is not configured.
     */
    private fun checkIsConfigured() {
        check(isConfigured) { "UxCam is not configured. Call build() before using it." }
    }

    /**
     * Checks if a session has been started.
     *
     * @throws IllegalStateException If a session has not been started.
     */
    private fun checkIsSessionStarted() {
        check(isSessionStarted) { "Session is not started. Call startSession() before using it." }
    }

    /**
     * Starts a new session with the provided user data.
     */
    suspend fun startSession() {
        checkIsConfigured()
        isSessionStarted = true
        sessionManager?.startSession(
            name = name,
            email = email,
            packageName = packageName,
            deviceId = deviceId
        )
    }

    /**
     * Ends the current session.
     */
    suspend fun endSession() {
        checkIsConfigured()
        isSessionStarted = false
        sessionManager?.endSession()
    }

    /**
     * Adds an event to the current session.
     *
     * @param name The name of the event.
     * @param properties A map of event properties.
     */
    suspend fun addEvent(name: String, properties: Map<String, Any>) {
        checkIsSessionStarted()
        sessionManager?.addEvent(name, properties)
    }

    /**
     * Attaches a listener for navigation events in the app, which tracks changes in navigation.
     *
     * @param navController The navigation controller to listen to.
     */
    @Suppress("unused")
    fun attachNavigationListener(navController: NavController) {
        checkIsConfigured()
        navController.addOnDestinationChangedListener { _, destination, arguments ->
            val destinationName = destination.label ?: "Unknown Destination"
            val args = arguments?.keySet()?.associateWith { arguments[it] } ?: emptyMap()

            CoroutineScope(Dispatchers.IO).launch {
                sessionManager?.addEvent(
                    Events.NAVIGATION,
                    mapOf("destination" to destinationName, "arguments" to args)
                )
            }
        }
    }

    /**
     * Predefined event keys for UXCam events.
     */
    object Events {
        const val NAVIGATION = "NavigationEvent"
        const val BUTTON_CLICK = "ButtonClickEvent"
        const val SCREEN_VIEW = "ScreenViewEvent"
        const val API_CALL = "ApiCallEvent"
    }
}

