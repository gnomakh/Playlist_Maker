package com.practicum.playlistmaker.domain.use_case

import com.practicum.playlistmaker.domain.models.Track

interface HistoryInteractor {
    fun getHistory() : ArrayList<Track>
    fun saveHistory(array : ArrayList<Track>)
    fun clearHistory()
}