package com.practicum.playlistmaker.domain.repository

import com.practicum.playlistmaker.domain.player.PlayerListener

interface PlayerRepository {
    fun preparePlayer(trackUrl: String, playerListener: PlayerListener)
    fun startPlayer(playerListener: PlayerListener)
    fun pausePlayer(playerListener: PlayerListener)
    fun getCurrentTime() : String
    fun releaseMediaPlayer()
    fun playbackControl(playerListener: PlayerListener)
}