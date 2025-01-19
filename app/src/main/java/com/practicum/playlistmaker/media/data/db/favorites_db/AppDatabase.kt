package com.practicum.playlistmaker.media.data.db.favorites_db

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    version = 2,
    entities = [
        TrackEntity::class
    ]
)
abstract class AppDatabase: RoomDatabase() {

    abstract fun getTrackDao(): TrackDao
}