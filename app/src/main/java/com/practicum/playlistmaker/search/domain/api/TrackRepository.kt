package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface TrackRepository {
    fun searchTracks(expression: String): Flow<Pair<List<Track>?, String?>>
}