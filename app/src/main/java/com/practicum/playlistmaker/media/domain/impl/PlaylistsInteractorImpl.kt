package com.practicum.playlistmaker.media.domain.impl

import com.practicum.playlistmaker.media.domain.api.PlaylistsInteractor
import com.practicum.playlistmaker.media.domain.api.PlaylistsRepository
import com.practicum.playlistmaker.media.domain.models.Playlist

class PlaylistsInteractorImpl(private val playlistsRepository: PlaylistsRepository) : PlaylistsInteractor {
    override suspend fun addPlaylist(playlist: Playlist) {
        playlistsRepository.addPlaylist(playlist)
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        playlistsRepository.deletePlaylisst(playlist)
    }

}