package com.practicum.playlistmaker.media.domain.impl

import com.practicum.playlistmaker.media.domain.api.RelativityInteractor
import com.practicum.playlistmaker.media.domain.api.RelativityRepository
import com.practicum.playlistmaker.media.domain.models.Playlist
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class RelativityInteractorImpl(private val relativityRepository: RelativityRepository) : RelativityInteractor {
    override suspend fun addConnection(track: Track, playlist: Playlist) {
        relativityRepository.addConnection(track, playlist)
    }
    override suspend fun deleteConnection(track: Track, playlist: Playlist) {
        relativityRepository.deleteConnection(track, playlist)
    }
    override suspend fun getTrackIds() : Flow<List<Int>> {
        return relativityRepository.getTrackIds()
    }

}