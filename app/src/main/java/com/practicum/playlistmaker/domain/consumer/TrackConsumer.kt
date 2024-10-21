package com.practicum.playlistmaker.domain.consumer

import com.practicum.playlistmaker.domain.models.Track

interface TrackConsumer {
    fun onSuccess(response : ArrayList<Track>)
    fun onFailure(resultCode : Int)
}