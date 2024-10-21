package com.practicum.playlistmaker.domain.use_case

import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.repository.TrackListRepository

class TrackListInteractor(val trackListRepository: TrackListRepository) {
    fun getHistory(key: String) {
        trackListRepository.getHistory(key)
    }
    fun saveHistory(array : ArrayList<Track>, key: String) {
        trackListRepository.saveHistory(key, array)
    }

    companion object {
        const val PREFS_KEY = "key_for_prefs"
        const val TRACK_KEY = "track_key"
        const val TRACK_LIST_KEY = "track_list_key"
    }
}