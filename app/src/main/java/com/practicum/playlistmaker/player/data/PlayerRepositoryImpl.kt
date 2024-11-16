package com.practicum.playlistmaker.player.data

import android.media.MediaPlayer
import com.practicum.playlistmaker.PlayerListener
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.player.domain.api.PlayerRepository
import com.practicum.playlistmaker.search.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerRepositoryImpl() : PlayerRepository {

    private var mediaPlayer = MediaPlayer()
    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
    private val searchHistoryInteractor = Creator.provideHistoryInteractor()

    override fun preparePlayer(): Track {
        val trackData = searchHistoryInteractor.getHistory()[0]
        if(trackData.previewUrl != "No previewUrl") {
            mediaPlayer = mediaPlayer.apply {
                setDataSource(trackData.previewUrl)
                prepare()
            }
        }
    }

    override fun startPlayer(playerListener: PlayerListener) {
        mediaPlayer.start()
        playerListener.onPlayerStart()
        playerState = PLAYING_STATE
    }

    override fun pausePlayer() {
        mediaPlayer.apply {
            start()
            status.onPlay()
            handler?.removeCallbacksAndMessages(null)
            val updateProgressTask = object : Runnable {
                override fun run() {
                    if (mediaPlayer?.isPlaying == true) {
                        val progress = getTime()
                        status.onProgress(progress)
                        handler.postDelayed(this, 500)
                    }
                }
            }
            handler.post(updateProgressTask)

            setOnCompletionListener {
                handler?.removeCallbacksAndMessages(null)
                status.onStop()
                status.onProgress("00:00")
            }
        }
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
        return dateFormat.format(mediaPlayer.currentPosition)
    }

    companion object {
        const val DEFAULT_STATE = "DEFAULT"
        const val PREPARED_STATE = "PREPARED"
        const val PLAYING_STATE = "PLAYING"
        const val PAUSED_STATE = "PAUSED"
    }
}