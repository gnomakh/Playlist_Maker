package com.practicum.playlistmaker.playlist.ui.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.media.domain.api.PlaylistsGetterUseCase
import com.practicum.playlistmaker.media.domain.api.PlaylistsInteractor
import com.practicum.playlistmaker.media.domain.models.Playlist
import com.practicum.playlistmaker.playlist.domain.api.PlaylistRepository
import com.practicum.playlistmaker.root.getDeclination
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val application: Application,
    private val playlistsGetterUseCase: PlaylistsGetterUseCase,
    private val playlistRepo: PlaylistRepository
) : AndroidViewModel(application) {

    private val playlistStateLiveData = MutableLiveData<Playlist>()

    fun getPlaylistLiveData(): LiveData<Playlist> {
        return playlistStateLiveData
    }

    private val tracksLiveData = MutableLiveData<List<Track>>()

    fun getTracksLiveData(): LiveData<List<Track>> {
        return tracksLiveData
    }

    var id = "0"

    fun setPlaylistData(id: String) {

        this.id = id
        viewModelScope.launch(Dispatchers.IO) {
            playlistStateLiveData.postValue(playlistsGetterUseCase.getPlaylist(id.toInt()))
        }
    }

    fun getTrackList() {
        viewModelScope.launch(Dispatchers.IO) {
            while (playlistStateLiveData.value == null) {
                setPlaylistData(id)
            }

            playlistRepo.getTrackList(playlistStateLiveData.value!!).collect { trackList ->
                if (trackList.isNotEmpty()) tracksLiveData.postValue(trackList)
                else tracksLiveData.postValue(listOf())
            }
        }
    }

    fun deleteTrack(track: Track) {
        viewModelScope.launch {
            playlistRepo.deleteTrack(track, playlistStateLiveData.value?.id!!)
            getTrackList()
        }
    }

    fun deletePlaylist() {
        viewModelScope.launch(Dispatchers.IO) {
            playlistRepo.deletePlaylist(playlistStateLiveData.value!!)
        }
    }

    fun getIntentMessage() : String {
        val trackCount = "${playlistStateLiveData.value!!.tracksCount} ${application.getDeclination(playlistStateLiveData.value!!.tracksCount)}"
        return playlistRepo.getShareMessage(tracksLiveData.value!!, playlistStateLiveData.value!!, trackCount)
    }
}