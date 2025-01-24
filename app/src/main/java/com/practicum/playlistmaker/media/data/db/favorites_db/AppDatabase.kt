package com.practicum.playlistmaker.media.data.db.favorites_db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.playlistmaker.media.data.db.dao.PlaylistDao
import com.practicum.playlistmaker.media.data.db.dao.FavoritesDao
import com.practicum.playlistmaker.media.data.db.dao.RelativeDbDao
import com.practicum.playlistmaker.media.data.db.entities.TrackEntity
import com.practicum.playlistmaker.media.data.db.entities.PlaylistEntity
import com.practicum.playlistmaker.media.data.db.entities.RelativeDbEntity


@Database(
    version = 1,
    entities = [
        TrackEntity::class,
        PlaylistEntity::class,
        RelativeDbEntity::class

    ]
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun getTrackDao(): FavoritesDao
    abstract fun getPlaylistDao(): PlaylistDao
    abstract fun getRelativeDbDao(): RelativeDbDao
}