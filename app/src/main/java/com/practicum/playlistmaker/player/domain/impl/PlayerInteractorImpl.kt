package com.practicum.playlistmaker.player.domain.impl

import com.practicum.playlistmaker.player.domain.api.PlayerRepository
import com.practicum.playlistmaker.player.domain.api.PlayerInteractor

class PlayerInteractorImpl(val playerRepository : PlayerRepository) : PlayerInteractor {
    override fun preparePlayer(
        trackUrl: String,
        onPreparedListener: () -> Unit,
        onCompletionListener: () -> Unit
    ) {
        playerRepository.preparePlayer(
            trackUrl,
            onPreparedListener,
            onCompletionListener
        )
    }

    override fun startPlayer() {
        playerRepository.startPlayer()
    }

    override fun pausePlayer() {
        playerRepository.pausePlayer()
    }

    override fun getCurrentTime(): String {
        return playerRepository.getCurrentTime()
    }

    override fun releaseMediaPlayer() {
        playerRepository.releasePlayer()
    }

}