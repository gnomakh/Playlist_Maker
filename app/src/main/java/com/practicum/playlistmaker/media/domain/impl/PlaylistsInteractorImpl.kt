package com.practicum.playlistmaker.media.domain.impl

import com.practicum.playlistmaker.media.domain.api.PlaylistsInteractor
import com.practicum.playlistmaker.media.domain.api.PlaylistsRepository
import com.practicum.playlistmaker.media.domain.models.Playlist
import com.practicum.playlistmaker.search.domain.models.Track

class PlaylistsInteractorImpl(private val playlistsRepository: PlaylistsRepository) : PlaylistsInteractor {
    override suspend fun addPlaylist(playlist: Playlist) {
        playlistsRepository.addPlaylist(playlist)
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        playlistsRepository.deletePlaylisst(playlist)
    }

    override suspend fun addTrackToPlaylist(track: Track, playlist: Playlist) : Boolean {
        return playlistsRepository.addTrackToPlaylist(track, playlist)
    }

    override suspend fun deleteTrackFromPlaylist(track: Track, playlist: Playlist) {
        playlistsRepository.deleteTrackFromPlaylist(track, playlist)
    }

    override suspend fun getTrackIdList(playlist: Playlist): List<Int> {
        return playlistsRepository.getTrackIdList(playlist)
    }

}