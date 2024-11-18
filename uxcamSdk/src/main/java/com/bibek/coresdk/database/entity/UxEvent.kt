package com.bibek.coresdk.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity()
data class UxEvent(
    @PrimaryKey() val id: String,
    val sessionId: String,
    val name: String,
    @ColumnInfo(name = "properties") val propertiesJson: String
)