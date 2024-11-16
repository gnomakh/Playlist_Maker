package com.practicum.playlistmaker.player.domain.api

import com.practicum.playlistmaker.PlayerListener

interface PlayerInteractor {
    fun preparePlayer(): Track
    fun startPlayer(playerListener: PlayerListener)
    fun pausePlayer(playerListener: PlayerListener)
    fun getCurrentTime() : String
    fun releaseMediaPlayer()
    fun playbackControl(playerListener: PlayerListener)
}