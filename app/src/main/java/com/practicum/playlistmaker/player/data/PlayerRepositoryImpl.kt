package com.practicum.playlistmaker.player.data

import android.media.MediaPlayer
import com.practicum.playlistmaker.player.domain.api.PlayerRepository
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerRepositoryImpl(var mediaPlayer: MediaPlayer) : PlayerRepository {

    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }

    override fun preparePlayer(
        trackUrl: String,
        onPreparedListener: () -> Unit,
        onCompletionListener: () -> Unit
    ) {
        if (trackUrl != "No previewUrl") {
            mediaPlayer = mediaPlayer.apply {
                setDataSource(trackUrl)
                prepareAsync()
                setOnPreparedListener { onPreparedListener() }
                setOnCompletionListener { onCompletionListener() }
            }
        }
    }

    override fun startPlayer() {
        mediaPlayer.start()
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
    }

    override fun releasePlayer() {
        mediaPlayer.release()
    }

    override fun getCurrentTime(): String {
        return dateFormat.format(mediaPlayer.currentPosition)
    }

}