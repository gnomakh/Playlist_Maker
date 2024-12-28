package com.example.courutines.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TrackDao {

    @Insert(entity = TrackEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackEntity)

    @Delete(entity = TrackEntity::class)
    suspend fun deleteTrack(trackEntity: TrackEntity)

    @Query("SELECT * FROM track_table")
    suspend fun getTrackList(): List<TrackEntity>

    @Query("SELECT trackId FROM track_table")
    suspend fun getTracksId(): List<Int>

}