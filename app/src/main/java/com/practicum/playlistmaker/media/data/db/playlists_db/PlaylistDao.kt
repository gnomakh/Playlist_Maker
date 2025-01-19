package com.practicum.playlistmaker.media.data.db.playlists_db

import androidx.room.*
import com.practicum.playlistmaker.media.domain.models.Playlist

@Dao
interface PlaylistDao {
    @Insert(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistEntity)
    @Delete(entity = PlaylistEntity::class)
    suspend fun deletePlaylist(playlist: PlaylistEntity)
    @Query("SELECT * FROM playlists_table ORDER BY addedAt DESC")
    suspend fun getPlaylists(): List<PlaylistEntity>
}