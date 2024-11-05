package com.practicum.playlistmaker.domain.repository

import com.practicum.playlistmaker.domain.models.Track

interface HistoryRepository {
    fun saveHistory(array: ArrayList<Track>,)
    fun getHistory() : ArrayList<Track>?
    fun clearHistory()
}