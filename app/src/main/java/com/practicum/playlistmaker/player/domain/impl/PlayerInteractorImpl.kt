package com.practicum.playlistmaker.player.domain.impl

import com.practicum.playlistmaker.PlayerListener
import com.practicum.playlistmaker.player.domain.api.PlayerRepository
import com.practicum.playlistmaker.player.domain.api.PlayerInteractor
import com.practicum.playlistmaker.search.domain.models.Track

class PlayerInteractorImpl(val playerRepository : PlayerRepository) : PlayerInteractor {
    override fun preparePlayer(): Track {
        return playerRepository.preparePlayer()
    }

    override fun startPlayer(playerListener: PlayerListener) {
        playerRepository.startPlayer(playerListener)
    }

    override fun pausePlayer(playerListener: PlayerListener) {
        playerRepository.pausePlayer(playerListener)
    }

    override fun getCurrentTime(): String {
        return playerRepository.getCurrentTime()
    }

    override fun releaseMediaPlayer() {
        playerRepository.releaseMediaPlayer()
    }

    override fun playbackControl(playerListener: PlayerListener) {
        playerRepository.playbackControl(playerListener)
    }

}