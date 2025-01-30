package com.practicum.playlistmaker.media.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker.media.data.db.entities.RelativeDbEntity


@Dao
interface RelativeDbDao {

    @Insert(entity = RelativeDbEntity::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun addConnection(relativeDbEntity: RelativeDbEntity): Long

    @Query("DELETE FROM relative_table WHERE trackId = :trackId AND playlistId = :playlistId")
    suspend fun deleteConnection(trackId: Int, playlistId: Int)

    @Query("DELETE FROM relative_table WHERE playlistId = :playlistId")
    suspend fun deletePlaylistRelations(playlistId: Int)

    @Query("SELECT trackId FROM relative_table")
    suspend fun getTrackIds() : List<Int>

    @Query("SELECT trackId FROM relative_table WHERE playlistId = :id ORDER BY keyId")
    suspend fun getPlaylistEntries(id: Int) : List<Int>

    @Query("SELECT COUNT(*) FROM relative_table WHERE trackId = :trackId")
    suspend fun hasRelationships(trackId: Int): Boolean
}