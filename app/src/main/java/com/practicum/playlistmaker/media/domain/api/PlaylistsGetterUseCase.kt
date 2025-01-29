package com.practicum.playlistmaker.media.domain.api

import com.practicum.playlistmaker.media.domain.models.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistsGetterUseCase {
    suspend fun getPlaylists() : Flow<List<Playlist>>
    suspend fun getPlaylist(id: Int) : Playlist
}