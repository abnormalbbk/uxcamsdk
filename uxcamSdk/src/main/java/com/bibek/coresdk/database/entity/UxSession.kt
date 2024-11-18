package com.bibek.coresdk.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UxSession(
    @PrimaryKey val id: String,
    val startTime: Long,
    var endTime: Long? = null,
    val email: String,
    val name: String,
    val deviceId: String,
    val packageName: String
)

