package com.practicum.playlistmaker.search.domain.impl

import com.practicum.playlistmaker.search.domain.api.HistoryInteractor
import com.practicum.playlistmaker.search.domain.api.HistoryRepository
import com.practicum.playlistmaker.search.domain.models.Track

class HistoryInteractorImpl(val historyRepository: HistoryRepository) : HistoryInteractor {
    override fun addtrackToHistory(array: ArrayList<Track>, track: Track) {
        historyRepository.addTrackToHistory(array, track)
    }

    override fun getHistory(): ArrayList<Track> {
        return historyRepository.getHistory() ?: arrayListOf()
    }

    override fun clearHistory() {
        historyRepository.clearHistory()
    }
}