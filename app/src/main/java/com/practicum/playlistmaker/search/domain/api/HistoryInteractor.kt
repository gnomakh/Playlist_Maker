package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface HistoryInteractor {
    fun addtrackToHistory(track: Track)
    fun getHistory(): ArrayList<Track>
    fun clearHistory()
}