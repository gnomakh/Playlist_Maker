package com.practicum.playlistmaker.search.domain.impl

import com.practicum.playlistmaker.search.domain.api.HistoryInteractor
import com.practicum.playlistmaker.search.domain.api.HistoryRepository
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class HistoryInteractorImpl(val historyRepository: HistoryRepository) : HistoryInteractor {
    override suspend fun addtrackToHistory(track: Track) {
        historyRepository.addTrackToHistory(track)
    }

    override fun getHistory(): Flow<List<Track>> {
        return historyRepository.getHistory()
    }

    override fun clearHistory() {
        historyRepository.clearHistory()
    }
}