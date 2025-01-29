package com.practicum.playlistmaker.playlist.domain.api

import com.practicum.playlistmaker.media.domain.models.Playlist
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    suspend fun getTrackList(playlist: Playlist): Flow<List<Track>>
    suspend fun deleteTrack(track: Track, playlistId: Int)
    suspend fun deletePlaylist(playlist: Playlist)
    suspend fun updatePlaylist(id: Int, title: String, description: String, picture: String?)
    fun getShareMessage(tracks: List<Track>, playlist: Playlist, trackCount: String) : String
}