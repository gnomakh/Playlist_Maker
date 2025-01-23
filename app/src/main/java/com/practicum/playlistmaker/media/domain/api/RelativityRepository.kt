package com.practicum.playlistmaker.media.domain.api

import com.practicum.playlistmaker.media.domain.models.Playlist
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface RelativityRepository {
    suspend fun addConnection(track: Track, playlist: Playlist)
    suspend fun deleteConnection(track: Track, playlist: Playlist)
    suspend fun getTrackIds() : Flow<List<Int>>
}