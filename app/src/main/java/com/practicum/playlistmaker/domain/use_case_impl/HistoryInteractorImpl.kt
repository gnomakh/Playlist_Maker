package com.practicum.playlistmaker.domain.use_case_impl

import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.repository.HistoryRepository
import com.practicum.playlistmaker.domain.use_case.HistoryInteractor

class HistoryInteractorImpl(val historyRepository: HistoryRepository) : HistoryInteractor {
    override fun getHistory() : ArrayList<Track> {
        return historyRepository.getHistory() ?: arrayListOf()
    }
    override fun saveHistory(array : ArrayList<Track>) {
        historyRepository.saveHistory(array)
    }

    override fun clearHistory() {
        historyRepository.clearHistory()
    }
}