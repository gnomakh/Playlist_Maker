package com.practicum.playlistmaker.domain.use_case

import com.practicum.playlistmaker.domain.player.PlayerListener

interface PlayerInteractor {
    fun preparePlayer(trackUrl: String, playerListener: PlayerListener)
    fun startPlayer(playerListener: PlayerListener)
    fun pausePlayer(playerListener: PlayerListener)
    fun getCurrentTime() : String
    fun releaseMediaPlayer()
    fun playbackControl(playerListener: PlayerListener)
}