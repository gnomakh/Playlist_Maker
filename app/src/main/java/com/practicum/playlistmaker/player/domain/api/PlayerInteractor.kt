package com.practicum.playlistmaker.player.domain.api

import com.practicum.playlistmaker.player.ui.state.PlaybackState


interface PlayerInteractor {
    fun preparePlayer(
        trackUrl: String,
        onPreparedListener: () -> Unit,
        onCompletionListener: () -> Unit
    )
    fun startPlayer()
    fun pausePlayer()
    fun getCurrentTime() : String
    fun releaseMediaPlayer()
}