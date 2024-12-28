package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {
    fun addTrackToHistory(track: Track)
    fun getHistory(): ArrayList<Track>
    fun clearHistory()
}