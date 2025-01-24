package com.practicum.playlistmaker.media.data.db.dao

import androidx.room.*
import com.practicum.playlistmaker.media.data.db.entities.PlaylistEntity

@Dao
interface PlaylistDao {
    @Insert(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistEntity)

    @Delete(entity = PlaylistEntity::class)
    suspend fun deletePlaylist(playlist: PlaylistEntity)

    @Query("SELECT * FROM playlists_table ORDER BY addedAt DESC")
    suspend fun getPlaylists(): List<PlaylistEntity>
}