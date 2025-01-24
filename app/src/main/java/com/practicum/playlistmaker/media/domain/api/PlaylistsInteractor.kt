package com.practicum.playlistmaker.media.domain.api

import com.practicum.playlistmaker.media.domain.models.Playlist
import com.practicum.playlistmaker.search.domain.models.Track

interface PlaylistsInteractor {
    suspend fun addPlaylist(playlist: Playlist)
    suspend fun deletePlaylist(playlist: Playlist)
    suspend fun addTrackToPlaylist(track: Track, playlist: Playlist) : Boolean

    suspend fun deleteTrackFromPlaylist(track: Track, playlist: Playlist)

    suspend fun getTrackIdList(playlist: Playlist): List<Int>
}