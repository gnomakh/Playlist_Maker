package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface HistoryInteractor {
    suspend fun addtrackToHistory(track: Track)
    fun getHistory(): Flow<ArrayList<Track>>
    fun clearHistory()
}