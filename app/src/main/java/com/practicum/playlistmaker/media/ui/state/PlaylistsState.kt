package com.practicum.playlistmaker.media.ui.state

import com.practicum.playlistmaker.media.domain.api.FavoritesInteractor
import com.practicum.playlistmaker.media.domain.models.Playlist
import com.practicum.playlistmaker.search.domain.models.Track

sealed class PlaylistsState {
    data class Content(val playlists: List<Playlist>) : PlaylistsState()
    object Empty : PlaylistsState()

}