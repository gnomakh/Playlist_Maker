package com.practicum.playlistmaker.domain.use_case

import com.practicum.playlistmaker.domain.models.Track

interface HistoryInteractor {
    fun addtrackToHistory(array: ArrayList<Track>, track: Track) : ArrayList<Track>
    fun getHistory() : ArrayList<Track>
    fun saveHistory(array : ArrayList<Track>)
    fun clearHistory()
}