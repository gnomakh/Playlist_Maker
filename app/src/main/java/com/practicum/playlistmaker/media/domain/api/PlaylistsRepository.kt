package com.practicum.playlistmaker.media.domain.api

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.media.domain.models.Playlist
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {
    suspend fun addPlaylist(playlist: Playlist)

    suspend fun deletePlaylist(playlist: Playlist)

    fun getPlaylists() : Flow<List<Playlist>>

    suspend fun getPlaylist(id: Int) : Playlist

    suspend fun addTrackToPlaylist(track: Track, playlist: Playlist) : Boolean

}