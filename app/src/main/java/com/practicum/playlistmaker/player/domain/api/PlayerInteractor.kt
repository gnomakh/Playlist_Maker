package com.practicum.playlistmaker.player.domain.api

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