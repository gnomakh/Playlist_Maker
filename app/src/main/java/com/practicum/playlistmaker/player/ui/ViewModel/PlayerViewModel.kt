package com.practicum.playlistmaker.player.ui.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.media.domain.api.FavoritesInteractor
import com.practicum.playlistmaker.media.domain.api.PlaylistsGetterUseCase
import com.practicum.playlistmaker.media.domain.api.PlaylistsInteractor
import com.practicum.playlistmaker.media.domain.api.RelativityInteractor
import com.practicum.playlistmaker.media.domain.models.Playlist
import com.practicum.playlistmaker.media.ui.state.PlaylistsState
import com.practicum.playlistmaker.player.domain.api.PlayerInteractor
import com.practicum.playlistmaker.player.ui.state.PlaybackState
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val playerInteractor: PlayerInteractor,
    private val favoritesInteractor: FavoritesInteractor,
    private val playlistsGetter: PlaylistsGetterUseCase,
    private val relativityInteractor: RelativityInteractor,
    private val playlistsInteractor: PlaylistsInteractor
) : ViewModel() {

    private var timerJob: Job? = null
    private var isPlaying = false

    private val trackInfoLiveData = MutableLiveData<Track>()
    fun getTrackInfoLiveData(): LiveData<Track> {
        return trackInfoLiveData
    }

    private val playbackTimeLiveData = MutableLiveData<String>()
    fun getPlaybackTimeLiveData(): LiveData<String> {
        return playbackTimeLiveData
    }

    private var playerStateLiveData = MutableLiveData(PlaybackState.DEFAULT_STATE)
    fun getPlayerStateLiveData(): LiveData<PlaybackState> {
        return playerStateLiveData
    }

    private val playlistsLiveData = MutableLiveData<PlaylistsState>()
    fun getPlaylistsLiveData(): LiveData<PlaylistsState>? {
        return playlistsLiveData
    }

    fun initializeViewModel(track: Track) {
        viewModelScope.launch {
            trackInfoLiveData.postValue(track.apply {
                isFavorite = favoritesInteractor.isFavorite(track.trackId)
            })
            preparePlayer(track)
        }
    }

    private fun preparePlayer(track: Track) {
        isPlaying = false
        playerStateLiveData.postValue(PlaybackState.PREPARED_STATE)
        playerInteractor.preparePlayer(
            track.previewUrl,
            {
                playerStateLiveData.postValue(PlaybackState.PREPARED_STATE)
            },
            {
                playerStateLiveData.postValue(PlaybackState.PREPARED_STATE)
                playbackTimeLiveData.postValue(DEFAULT_TIME)
            })
    }

    private fun startPlayer() {
        isPlaying = true
        playerStateLiveData.postValue(PlaybackState.PLAYING_STATE)
        playerInteractor.startPlayer()
        postCurrentTime()
    }

    private fun pausePlayer() {
        isPlaying = false
        playerInteractor.pausePlayer()
        playerStateLiveData.postValue(PlaybackState.PAUSED_STATE)
    }

    fun playbackControl() {
        when (playerStateLiveData.value) {
            PlaybackState.PLAYING_STATE -> pausePlayer()
            PlaybackState.PAUSED_STATE, PlaybackState.PREPARED_STATE -> startPlayer()
            PlaybackState.DEFAULT_STATE -> {
                isPlaying = false
            }

            null -> {}
        }
    }

    fun onAppPause() {
        if (playerStateLiveData.value == PlaybackState.PLAYING_STATE) pausePlayer()
    }

    fun onFragmentDestroy() {
        playerInteractor.releaseMediaPlayer()
    }

    private fun postCurrentTime() {
        timerJob = viewModelScope.launch {
            while (isPlaying) {
                playbackTimeLiveData.postValue(
                    if (playerStateLiveData.value == PlaybackState.PREPARED_STATE)
                        DEFAULT_TIME
                    else playerInteractor.getCurrentTime()
                )
                delay(TIMER_DELAY)
            }
        }
    }

    fun onFavoriteClick() {
        viewModelScope.launch {
            val track = trackInfoLiveData.value
            var trackIdsInPlaylists = mutableListOf<Int>()
            relativityInteractor.getTrackIds().collect { list ->
                trackIdsInPlaylists = list as MutableList<Int>
            }
            if (track != null) {
                if (track.isFavorite) {
                    trackInfoLiveData.postValue(track.apply { isFavorite = false })
                    if (track.trackId !in trackIdsInPlaylists)
                        viewModelScope.launch { favoritesInteractor.deleteTrack(track) }
                    else
                        viewModelScope.launch {
                            favoritesInteractor.addTrack(track.apply {
                                isFavorite = false
                            })
                        }
                } else {
                    trackInfoLiveData.postValue(track.apply { isFavorite = true })
                    viewModelScope.launch {
                        favoritesInteractor.addTrack(track.apply {
                            isFavorite = true
                        })
                    }
                }
            }
        }
    }

    suspend fun addToPlaylist(playlist: Playlist): Boolean {
        val result = playlistsInteractor.addTrackToPlaylist(trackInfoLiveData.value!!, playlist)
        getPlaylists()
        return result
    }

    fun getPlaylists() {
        viewModelScope.launch {
            playlistsGetter.getPlaylists().collect { playlists ->
                if (playlists.isEmpty()) {
                    playlistsLiveData.postValue(PlaylistsState.Empty)
                } else {
                    playlistsLiveData.postValue(PlaylistsState.Content(playlists))
                }
            }
        }

    }

    companion object {
        private const val TIMER_DELAY = 300L
        private const val DEFAULT_TIME = "00:00"
    }
}

