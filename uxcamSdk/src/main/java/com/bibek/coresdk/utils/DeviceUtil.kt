package com.bibek.coresdk.utils

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings.Secure

object DeviceUtil {
    /**
     * Retrieves the unique device ID for the current Android device.
     * This method uses the `ANDROID_ID` from the `Secure` settings to obtain the device ID.
     *
     * @param context The application context used to access system settings.
     * @return A string representing the unique device ID.
     *
     * @SuppressLint("HardwareIds") Suppresses the lint warning about potentially unsafe access
     * to hardware identifiers.
     */

    @SuppressLint("HardwareIds")
    fun getDeviceId(context: Context): String {
        val string = Secure.getString(context.contentResolver, Secure.ANDROID_ID)
        return string
    }
}