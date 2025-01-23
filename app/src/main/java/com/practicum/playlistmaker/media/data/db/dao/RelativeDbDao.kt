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

    @Delete(entity = RelativeDbEntity::class)
    suspend fun deleteConnection(relativeDbEntity: RelativeDbEntity)

    @Query("SELECT trackId FROM relative_table")
    suspend fun getTrackIds() : List<Int>

    @Query("SELECT playlistId FROM relative_table WHERE playlistId = :id")
    suspend fun getPlaylistEntries(id: Int) : List<Int>
}