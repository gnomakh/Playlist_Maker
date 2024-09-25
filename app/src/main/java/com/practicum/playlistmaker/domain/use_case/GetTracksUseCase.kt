package com.practicum.playlistmaker.domain.use_case

import com.practicum.playlistmaker.domain.api.TrackRepository
import com.practicum.playlistmaker.domain.models.Track

class GetTracksUseCase(private val trackRepository: TrackRepository) {
    fun execute(expression: String): List<Track> {
        return trackRepository.searchTracks(expression)
    }
}