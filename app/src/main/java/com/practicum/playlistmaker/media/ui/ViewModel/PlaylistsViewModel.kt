package com.practicum.playlistmaker.media.ui.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PlaylistsViewModel : ViewModel() {
    private val playlists: MutableLiveData<Any>? = null
    fun getPlaylists() : LiveData<Any>? {
        return playlists
    }
}