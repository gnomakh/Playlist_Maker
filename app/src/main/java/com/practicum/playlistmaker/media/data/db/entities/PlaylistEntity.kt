package com.practicum.playlistmaker.media.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.ColumnInfo.Companion.TEXT
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var title: String,
    var description: String,
    var picture: String?,
    var tracksCount: Int,
    val addedAt: Long = System.currentTimeMillis()
)