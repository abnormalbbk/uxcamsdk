package com.bibek.coresdk.utils

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings.Secure

object DeviceUtil {
    @SuppressLint("HardwareIds")
    fun getDeviceId(context: Context): String {
        val string = Secure.getString(context.contentResolver, Secure.ANDROID_ID)
        return string
    }
}