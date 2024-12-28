package com.example.courutines.db

import androidx.room.ColumnInfo
import androidx.room.ColumnInfo.Companion.TEXT
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "track_table")
data class TrackEntity(

    @ColumnInfo(name = "trackName", typeAffinity = TEXT)
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: String,
    val artworkUrl100: String,

    @PrimaryKey(autoGenerate = true)
    val trackId: Int,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String,
    var isFavorite: Boolean
)