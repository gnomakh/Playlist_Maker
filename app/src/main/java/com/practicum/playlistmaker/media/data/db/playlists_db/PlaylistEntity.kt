package com.practicum.playlistmaker.media.data.db.playlists_db

import androidx.room.ColumnInfo
import androidx.room.ColumnInfo.Companion.TEXT
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @ColumnInfo(name = "title", typeAffinity = TEXT)
    val title: String,
    val description: String,
    val picture: String,
    var tracksCount: String,
    val addedAt: Long = System.currentTimeMillis()

)