package com.practicum.playlistmaker.player.domain.api

import com.practicum.playlistmaker.PlayerListener
import com.practicum.playlistmaker.search.domain.models.Track

interface PlayerRepository {
    fun preparePlayer(): Track
    fun startPlayer(playerListener: PlayerListener)
    fun pausePlayer(playerListener: PlayerListener)
    fun getCurrentTime() : String
    fun releaseMediaPlayer()
    fun playbackControl(playerListener: PlayerListener)
}