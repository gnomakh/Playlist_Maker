package com.practicum.playlistmaker.media.ui.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.media.domain.api.PlaylistsInteractor
import com.practicum.playlistmaker.media.domain.models.Playlist
import kotlinx.coroutines.launch

class PlaylistCreationViewModel(private val playlistInteractor : PlaylistsInteractor) : ViewModel() {

    fun addPlaylist(playlist: Playlist) {
        viewModelScope.launch { playlistInteractor.addPlaylist(playlist) }
    }
}