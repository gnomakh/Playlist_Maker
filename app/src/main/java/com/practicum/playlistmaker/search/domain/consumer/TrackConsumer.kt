package com.practicum.playlistmaker.search.domain.consumer

import com.practicum.playlistmaker.search.domain.models.Track

interface TrackConsumer {
    fun onSuccess(response: ArrayList<Track>)
    fun onNoResult()
    fun onNetworkError()
}