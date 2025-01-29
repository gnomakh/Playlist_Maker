package com.practicum.playlistmaker.media.data.db.dao

import androidx.room.*
import com.practicum.playlistmaker.media.data.db.entities.PlaylistEntity

@Dao
interface PlaylistDao {
    @Insert(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistEntity)

    @Query("DELETE FROM playlists_table WHERE id = :playlistId")
    suspend fun deletePlaylist(playlistId: Int)

    @Query("SELECT * FROM playlists_table ORDER BY addedAt DESC")
    suspend fun getPlaylists(): List<PlaylistEntity>

    @Query("SELECT * FROM playlists_table WHERE id =:id")
    suspend fun getPlaylist(id: Int): PlaylistEntity

    @Query("UPDATE playlists_table SET title = :title, description = :description, picture = :picture WHERE id = :id")
    suspend fun updatePlaylist(id: Int, title: String, description: String, picture: String?)
}