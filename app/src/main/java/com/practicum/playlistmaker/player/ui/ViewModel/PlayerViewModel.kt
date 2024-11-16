package com.practicum.playlistmaker.player.ui.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.player.ui.state.PlaybackState
import com.practicum.playlistmaker.player.ui.state.PlayerState
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.ui.ViewModel.SearchViewModel
import com.practicum.playlistmaker.search.ui.state.SearchScreenState

class PlayerViewModel(): ViewModel() {

    private val playerInteractor = Creator.providePlayerInteractor()

    private val trackOnPlayerLiveData = MutableLiveData<Track>()
    fun getTrackOnPlayerLiveData(): LiveData<Track> = trackOnPlayerLiveData

    private val playStatusLiveData = MutableLiveData<PlaybackState>()
    fun getPlayStatusLiveData(): LiveData<PlaybackState> = playStatusLiveData

    init {
        preparePlayer()
    }

    fun preparePlayer() {
        trackOnPlayerLiveData.value = playerInteractor.preparePlayer()
    }



    companion object {
        fun factory(): ViewModelProvider.Factory {
            return viewModelFactory {
                initializer {
                    PlayerViewModel()
                }
            }
        }
    }
}

