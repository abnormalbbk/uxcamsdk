package com.bibek.coresdk.extensions

import com.bibek.coresdk.utils.UxLogger
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


fun Map<String, Any>.toJson(): String {
    val gson = Gson()
    return gson.toJson(this)
}

fun String?.toMap(): Map<String, Any> {
    if (this.isNullOrEmpty()) return mapOf()

    val gson = Gson()
    val type = object : TypeToken<Map<String, Any>>() {}.type
    try {
        return gson.fromJson(this, type)
    } catch (e: Exception) {
        UxLogger.e(message = "toMap  : ${e.printStackTrace()}}")
        return mapOf()
    }
}