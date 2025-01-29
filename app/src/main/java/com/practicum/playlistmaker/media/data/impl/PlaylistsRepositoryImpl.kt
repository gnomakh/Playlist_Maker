package com.practicum.playlistmaker.media.data.impl

import com.practicum.playlistmaker.media.data.db.converters.PlaylistDbConverter
import com.practicum.playlistmaker.media.data.db.converters.TrackDbConverter
import com.practicum.playlistmaker.media.data.db.entities.RelativeDbEntity
import com.practicum.playlistmaker.media.data.db.favorites_db.AppDatabase
import com.practicum.playlistmaker.media.domain.api.PlaylistsRepository
import com.practicum.playlistmaker.media.domain.models.Playlist
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistsRepositoryImpl(
    private val dataBase: AppDatabase,
    private val playlistConverter: PlaylistDbConverter,
    private val trackConverter: TrackDbConverter
) : PlaylistsRepository {

    override suspend fun addPlaylist(playlist: Playlist) {
        dataBase.getPlaylistDao().insertPlaylist(playlistConverter.map(playlist))
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        dataBase.getPlaylistDao().deletePlaylist(playlist.id!!)
    }

    override fun getPlaylists(): Flow<List<Playlist>> = flow {
        emit(
            dataBase.getPlaylistDao().getPlaylists().map { playlistEntity ->
                playlistConverter.map(playlistEntity.apply {
                    tracksCount =
                        dataBase.getRelativeDbDao().getPlaylistEntries(playlistEntity.id).size
                })
            }
        )
    }

    override suspend fun getPlaylist(id: Int): Playlist {
        return (playlistConverter.map(dataBase.getPlaylistDao().getPlaylist(id))
            .apply { tracksCount = dataBase.getRelativeDbDao().getPlaylistEntries(id).size })
    }

    override suspend fun addTrackToPlaylist(track: Track, playlist: Playlist): Boolean {
        val plid = playlist.id!!
        dataBase.getTrackDao().insertTrack(trackConverter.map(track))
        val result = dataBase.getRelativeDbDao()
            .addConnection(RelativeDbEntity(trackId = track.trackId, playlistId = plid))
        return result != -1L
    }
}