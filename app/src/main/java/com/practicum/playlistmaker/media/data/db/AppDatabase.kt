package com.example.courutines.db

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    version = 1,
    entities = [
        TrackEntity::class
    ]
)
abstract class AppDatabase: RoomDatabase() {

    abstract fun getTrackDao(): TrackDao
}