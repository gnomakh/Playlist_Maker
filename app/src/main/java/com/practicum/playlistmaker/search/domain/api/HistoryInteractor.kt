package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.models.Track

interface HistoryInteractor {
    fun addtrackToHistory(array: ArrayList<Track>, track: Track)
    fun getHistory() : ArrayList<Track>
    fun clearHistory()
}