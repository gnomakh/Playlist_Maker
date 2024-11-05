package com.practicum.playlistmaker.data.repository_impl

import android.media.MediaPlayer
import com.practicum.playlistmaker.domain.player.PlayerListener
import com.practicum.playlistmaker.domain.repository.PlayerRepository
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerRepositoryImpl() : PlayerRepository {

    private var mediaPlayer = MediaPlayer()
    private var playerState = DEFAULT_STATE

    override fun preparePlayer(trackUrl: String, playerListener: PlayerListener) {
        mediaPlayer.setDataSource(trackUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = PREPARED_STATE
        }
        mediaPlayer.setOnCompletionListener {
            playerListener.onPlayerStop()
            playerState = PREPARED_STATE
        }
    }

    override fun startPlayer(playerListener: PlayerListener) {
        mediaPlayer.start()
        playerListener.onPlayerStart()
        playerState = PLAYING_STATE
    }

    override fun pausePlayer(playerListener: PlayerListener) {
        mediaPlayer.pause()
        playerListener.onPlayerPause()
        playerState = PAUSED_STATE
    }

    override fun playbackControl(playerListener: PlayerListener) {
        when(playerState) {
            PLAYING_STATE -> {
                pausePlayer(playerListener)
            }
            PAUSED_STATE, PREPARED_STATE -> {
                startPlayer(playerListener)
            }
        }
    }

    override fun releaseMediaPlayer() {
        mediaPlayer.release()
    }

    override fun getCurrentTime() : String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
    }

    companion object {
        const val DEFAULT_STATE = "DEFAULT"
        const val PREPARED_STATE = "PREPARED"
        const val PLAYING_STATE = "PLAYING"
        const val PAUSED_STATE = "PAUSED"
    }
}