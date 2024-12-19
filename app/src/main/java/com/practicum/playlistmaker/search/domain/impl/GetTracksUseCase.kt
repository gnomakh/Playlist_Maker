package com.practicum.playlistmaker.search.domain.impl

import com.practicum.playlistmaker.search.domain.api.TrackRepository
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetTracksUseCase(private val trackRepository: TrackRepository) {

    fun searchTracks(expression: String) : Flow<Pair<List<Track>?, String?>> {
        return trackRepository.searchTracks(expression).map { result ->
            Pair(result.first, result.second)
        }
    }
}