package com.practicum.playlistmaker.media.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker.media.data.db.entities.TrackEntity

@Dao
interface TrackDao {

    @Insert(entity = TrackEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackEntity)

    @Delete(entity = TrackEntity::class)
    suspend fun deleteTrack(trackEntity: TrackEntity)

    @Query("SELECT * FROM tracks_table WHERE isFavorite = 1 ORDER BY addedAt DESC")
    suspend fun getFavorites(): List<TrackEntity>

    @Query("SELECT trackId FROM tracks_table where isFavorite = 1")
    suspend fun getFavoritesId(): List<Int>

//    @Query("SELECT * FROM tracks_table WHERE trackId IN (:ids)")

    @Query("""
        SELECT DISTINCT tracks_table.* FROM tracks_table 
        INNER JOIN relative_table ON tracks_table.trackId = relative_table.trackId 
        WHERE relative_table.playlistId = :playlistId
        ORDER BY relative_table.keyId DESC
    """)
    suspend fun getPlaylistTracks(playlistId: Int): List<TrackEntity>

}