package com.practicum.playlistmaker.media.data.impl

import com.practicum.playlistmaker.media.data.db.entities.RelativeDbEntity
import com.practicum.playlistmaker.media.data.db.favorites_db.AppDatabase
import com.practicum.playlistmaker.media.domain.api.RelativityRepository
import com.practicum.playlistmaker.media.domain.models.Playlist
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RelativityRepositoryImpl(
    private val db: AppDatabase
) : RelativityRepository {
    override suspend fun addConnection(track: Track, playlist: Playlist) {
        val relativity = RelativeDbEntity(trackId = track.trackId, playlistId = playlist.id!!)
        db.getRelativeDbDao().addConnection(relativity)
    }

    override suspend fun deleteConnection(track: Track, playlist: Playlist) {
        db.getRelativeDbDao().deleteConnection(track.trackId, playlist.id!!)
    }

    override suspend fun getTrackIds(): Flow<List<Int>> = flow {
        emit(db.getRelativeDbDao().getTrackIds())
    }

}