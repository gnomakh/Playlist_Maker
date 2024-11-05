package com.practicum.playlistmaker.domain.use_case_impl

import com.practicum.playlistmaker.domain.player.PlayerListener
import com.practicum.playlistmaker.domain.repository.PlayerRepository
import com.practicum.playlistmaker.domain.use_case.PlayerInteractor

class PlayerInteractorImpl(val playerRepository : PlayerRepository) : PlayerInteractor {
    override fun preparePlayer(trackUrl: String, playerListener: PlayerListener) {
        playerRepository.preparePlayer(trackUrl, playerListener)
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