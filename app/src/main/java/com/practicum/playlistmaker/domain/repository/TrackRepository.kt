package com.practicum.playlistmaker.domain.repository

import com.practicum.playlistmaker.domain.models.Track

interface TrackRepository {
    var resultCode: Int
    fun searchTracks(expression: String) : ArrayList<Track>
}