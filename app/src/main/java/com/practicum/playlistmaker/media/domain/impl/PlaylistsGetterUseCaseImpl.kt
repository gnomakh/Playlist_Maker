package com.practicum.playlistmaker.media.domain.impl

import com.practicum.playlistmaker.media.domain.api.PlaylistsGetterUseCase
import com.practicum.playlistmaker.media.domain.api.PlaylistsRepository
import com.practicum.playlistmaker.media.domain.models.Playlist
import kotlinx.coroutines.flow.Flow

class PlaylistsGetterUseCaseImpl(private val playlistsRepository: PlaylistsRepository) : PlaylistsGetterUseCase {
    override suspend fun getPlaylists() : Flow<List<Playlist>> {
        return playlistsRepository.getPlaylists()
    }

    override suspend fun getPlaylist(id: Int): Playlist {
        return playlistsRepository.getPlaylist(id)
    }
}