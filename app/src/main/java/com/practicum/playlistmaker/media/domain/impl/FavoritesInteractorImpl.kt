package com.practicum.playlistmaker.media.domain.impl

import com.practicum.playlistmaker.media.domain.api.FavoritesInteractor
import com.practicum.playlistmaker.media.domain.api.FavoritesRepository
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class FavoritesInteractorImpl(val favoritesRepository: FavoritesRepository) : FavoritesInteractor {

    override suspend fun addTrack(track: Track) {
        favoritesRepository.addTrack(track)
    }

    override suspend fun deleteTrack(track: Track) {
        favoritesRepository.deleteTrack(track)
    }

    override fun getFavorites() : Flow<List<Track>> {
        return favoritesRepository.getFavorites()
    }
}