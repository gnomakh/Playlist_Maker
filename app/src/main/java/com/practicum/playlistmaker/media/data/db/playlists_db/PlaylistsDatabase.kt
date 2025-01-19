package com.practicum.playlistmaker.media.data.db.playlists_db

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    version = 1,
    entities = [
        PlaylistEntity::class
    ]
)
abstract class PlaylistsDatabase: RoomDatabase() {

    abstract fun getPlaylistDao(): PlaylistDao
}