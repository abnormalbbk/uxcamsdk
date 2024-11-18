package com.bibek.coresdk.core

import android.content.Context
import androidx.navigation.NavController
import com.bibek.coresdk.database.AppDatabase
import com.bibek.coresdk.utils.DeviceUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object UxCam {
    var isConfigured = false
        private set
    var isSessionStarted = false
        private set
    var packageName: String = ""
        private set
    var email: String = ""
        private set
    var name: String = ""
        private set
    var deviceId: String = ""
        private set
    var sessionManager: SessionManager? = null
        private set

    fun setIdentifier(packageName: String) = apply { this.packageName = packageName }
    fun setEmail(email: String) = apply { this.email = email }
    fun setName(name: String) = apply { this.name = name }

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

    private fun checkIsConfigured() {
        check(isConfigured) { "UxCam is not configured. Call build() before using it." }
    }

    private fun checkIsSessionStarted() {
        check(isSessionStarted) { "Session is not started. Call startSession() before using it." }
    }

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

    suspend fun endSession() {
        checkIsConfigured()
        isSessionStarted = false
        sessionManager?.endSession()
    }

    suspend fun addEvent(name: String, properties: Map<String, Any>) {
        checkIsSessionStarted()
        sessionManager?.addEvent(name, properties)
    }


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

    // Predefined UXCam event keys
    object Events {
        const val NAVIGATION = "NavigationEvent"
        const val BUTTON_CLICK = "ButtonClickEvent"
        const val SCREEN_VIEW = "ScreenViewEvent"
        const val API_CALL = "ApiCallEvent"
    }
}


