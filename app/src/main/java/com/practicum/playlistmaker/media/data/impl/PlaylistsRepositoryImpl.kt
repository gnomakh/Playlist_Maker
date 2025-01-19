package com.practicum.playlistmaker.media.data.impl

import com.practicum.playlistmaker.media.data.db.playlists_db.PlaylistDbConverter
import com.practicum.playlistmaker.media.data.db.playlists_db.PlaylistEntity
import com.practicum.playlistmaker.media.data.db.playlists_db.PlaylistsDatabase
import com.practicum.playlistmaker.media.domain.api.PlaylistsRepository
import com.practicum.playlistmaker.media.domain.models.Playlist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistsRepositoryImpl(
    private val playlistsDb: PlaylistsDatabase,
    private val converter: PlaylistDbConverter
) : PlaylistsRepository {
    override suspend fun addPlaylist(playlist: Playlist) {
        playlistsDb.getPlaylistDao().insertPlaylist(converter.map(playlist))
    }

    override suspend fun deletePlaylisst(playlist: Playlist) {
        playlistsDb.getPlaylistDao().deletePlaylist(converter.map(playlist))
    }

    override fun getPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = playlistsDb.getPlaylistDao().getPlaylists()
        emit(convert(playlists))
    }

    private fun convert(playlist: List<PlaylistEntity>): List<Playlist> {
        return playlist.map {
                playlistItem -> converter.map(playlistItem)
        }
    }

}