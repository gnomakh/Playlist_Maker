package com.practicum.playlistmaker.player.ui.ViewModel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.player.domain.api.PlayerInteractor
import com.practicum.playlistmaker.player.ui.state.PlaybackState
import com.practicum.playlistmaker.search.domain.api.HistoryInteractor
import com.practicum.playlistmaker.search.domain.models.Track

class PlayerViewModel(
    val playerInteractor: PlayerInteractor,
    val historyInteractor: HistoryInteractor
) : ViewModel() {

    private val handler = Handler(Looper.getMainLooper())
    private val timeUpdateRunnable = createUpdateTimerRunnable()

    val trackOnPlayer = historyInteractor.getHistory()[0]

    private val trackInfoLiveData = MutableLiveData<Track>()
    fun getTrackInfoLiveData(): LiveData<Track> = trackInfoLiveData

    private val playbackTimeLiveData = MutableLiveData<String>()
    fun getPlaybackTimeLiveData(): LiveData<String> = playbackTimeLiveData

    private var playerStateLiveData = MutableLiveData(PlaybackState.DEFAULT_STATE)
    fun getPlayerStateLiveData(): LiveData<PlaybackState> = playerStateLiveData

    init {
        trackInfoLiveData.postValue(trackOnPlayer)
        preparePlayer()
    }

    private fun preparePlayer() {
        playerStateLiveData.postValue(PlaybackState.PREPARED_STATE)
        playerInteractor.preparePlayer(
            trackOnPlayer.previewUrl,
            {
                playerStateLiveData.postValue(PlaybackState.PREPARED_STATE)
            },
            {
                playerStateLiveData.postValue(PlaybackState.PREPARED_STATE)
            })
    }

    private fun startPlayer() {
        playerInteractor.startPlayer()
        playerStateLiveData.postValue(PlaybackState.PLAYING_STATE)
        handler.post(timeUpdateRunnable)
    }

    private fun pausePlayer() {
        playerInteractor.pausePlayer()
        playerStateLiveData.postValue(PlaybackState.PAUSED_STATE)
        handler.removeCallbacks(timeUpdateRunnable)
    }

    fun playbackControl() {
        when (playerStateLiveData.value) {
            PlaybackState.PLAYING_STATE -> pausePlayer()
            PlaybackState.PAUSED_STATE, PlaybackState.PREPARED_STATE -> startPlayer()
            PlaybackState.DEFAULT_STATE -> {}
            null -> {}

        }
    }

    fun onActivityPause() {
        if (playerStateLiveData.value == PlaybackState.PLAYING_STATE) pausePlayer()
    }

    private fun postCurrentTime() {
        playbackTimeLiveData.postValue(
            if (playerStateLiveData.value == PlaybackState.PREPARED_STATE)
                DEFAULT_TIME
            else
                playerInteractor.getCurrentTime()
        )
    }

    private fun createUpdateTimerRunnable(): Runnable {
        return object : Runnable {
            override fun run() {
                postCurrentTime()
                handler?.postDelayed(this, TIMER_DELAY)
            }
        }
    }

    companion object {
        private const val TIMER_DELAY = 500L
        private const val DEFAULT_TIME = "00:00"
    }
}

