package com.practicum.playlistmaker.media.domain.api

import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesInteractor {
    suspend fun addTrack(track: Track)
    suspend fun deleteTrack(track: Track)
    fun getFavorites() : Flow<List<Track>>

}