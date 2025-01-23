package com.practicum.playlistmaker.media.data.db.entities

import androidx.room.Entity
import androidx.room.*

@Entity(tableName = "relative_table",
    indices = [Index(value = ["trackId", "playlistId"], unique = true)])
data class RelativeDbEntity (
    @PrimaryKey(autoGenerate = true)
    val keyId: Int = 0,

    val trackId: Int,
    val playlistId: Int,
    val addedAt: Long = System.currentTimeMillis()

)