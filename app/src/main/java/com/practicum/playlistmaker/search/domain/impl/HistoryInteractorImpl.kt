package com.practicum.playlistmaker.search.domain.impl

import com.practicum.playlistmaker.search.domain.api.HistoryInteractor
import com.practicum.playlistmaker.search.domain.api.HistoryRepository
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class HistoryInteractorImpl(val historyRepository: HistoryRepository) : HistoryInteractor {
    override fun addtrackToHistory(track: Track) {
        historyRepository.addTrackToHistory(track)
    }

    override fun getHistory(): ArrayList<Track>{
        return historyRepository.getHistory()
    }

    override fun clearHistory() {
        historyRepository.clearHistory()
    }
}