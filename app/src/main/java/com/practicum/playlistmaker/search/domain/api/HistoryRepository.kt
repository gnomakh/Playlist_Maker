package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {
    suspend fun addTrackToHistory(track: Track)
    fun getHistory(): Flow<ArrayList<Track>>
    fun clearHistory()
}