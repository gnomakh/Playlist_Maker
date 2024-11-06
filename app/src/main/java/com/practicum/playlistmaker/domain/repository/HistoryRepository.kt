package com.practicum.playlistmaker.domain.repository

import com.practicum.playlistmaker.domain.models.Track

interface HistoryRepository {
    fun addTrackToHistory(array: ArrayList<Track>, track: Track) : ArrayList<Track>
    fun saveHistory(array: ArrayList<Track>,)
    fun getHistory() : ArrayList<Track>?
    fun clearHistory()
}