package com.practicum.playlistmaker.media.ui.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.media.domain.api.PlaylistsGetterUseCase
import com.practicum.playlistmaker.media.domain.api.PlaylistsInteractor
import com.practicum.playlistmaker.media.domain.api.PlaylistsRepository
import com.practicum.playlistmaker.media.domain.models.Playlist
import com.practicum.playlistmaker.media.ui.state.PlaylistsState
import kotlinx.coroutines.launch

class PlaylistsViewModel(private val playlistsGetter: PlaylistsGetterUseCase) : ViewModel() {

    private val playlistsLiveData = MutableLiveData<PlaylistsState>()

    fun getPlaylistsLiveData(): LiveData<PlaylistsState>? {
        return playlistsLiveData
    }

    fun getPlaylists() {
        Log.d("playlist111", "Плейлисты получаются")
        viewModelScope.launch {
            playlistsGetter.getPlaylists().collect { playlists ->
                if(playlists.isEmpty()) {
                    playlistsLiveData.postValue(PlaylistsState.Empty)
                    Log.d("playlist111", "Плейлистов не загружено")
                }
                else {
                    playlistsLiveData.postValue(PlaylistsState.Content(playlists))
                    Log.d("playlist111", "Плейлисты есть")
                }
            }
        }

    }
}