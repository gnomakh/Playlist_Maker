package com.practicum.playlistmaker.media.data.impl

import android.content.Context
import com.practicum.playlistmaker.media.data.db.converters.PlaylistDbConverter
import com.practicum.playlistmaker.media.data.db.entities.RelativeDbEntity
import com.practicum.playlistmaker.media.data.db.favorites_db.AppDatabase
import com.practicum.playlistmaker.media.domain.api.PlaylistsRepository
import com.practicum.playlistmaker.media.domain.models.Playlist
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistsRepositoryImpl(
    private val dataBase: AppDatabase,
    private val converter: PlaylistDbConverter,
    context: Context
) : PlaylistsRepository {

    override suspend fun addPlaylist(playlist: Playlist) {
        dataBase.getPlaylistDao().insertPlaylist(converter.map(playlist))
    }

    override suspend fun deletePlaylisst(playlist: Playlist) {
        dataBase.getPlaylistDao().deletePlaylist(converter.map(playlist))
    }

    override fun getPlaylists(): Flow<List<Playlist>> = flow {
        emit(
            dataBase.getPlaylistDao().getPlaylists().map { playlistEntity ->
                converter.map(playlistEntity.apply { tracksCount = dataBase.getRelativeDbDao().getPlaylistEntries(playlistEntity.id).size })
            }
        )
    }

    override suspend fun addTrackToPlaylist(track: Track, playlist: Playlist) : Boolean {
        val plid = playlist.id!!
        val result = dataBase.getRelativeDbDao().addConnection(RelativeDbEntity(trackId = track.trackId, playlistId = plid))
        return result != -1L
    }

    override suspend fun deleteTrackFromPlaylist(track: Track, playlist: Playlist) {

    }

    override suspend fun getTrackIdList(playlist: Playlist): List<Int> {

        return listOf()
    }
}