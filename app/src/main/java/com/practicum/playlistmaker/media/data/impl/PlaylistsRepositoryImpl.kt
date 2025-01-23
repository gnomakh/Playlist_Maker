package com.practicum.playlistmaker.media.data.impl

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.media.data.db.converters.PlaylistDbConverter
import com.practicum.playlistmaker.media.data.db.entities.RelativeDbEntity
import com.practicum.playlistmaker.media.data.db.favorites_db.AppDatabase
import com.practicum.playlistmaker.media.domain.api.PlaylistsRepository
import com.practicum.playlistmaker.media.domain.models.Playlist
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistsRepositoryImpl(
    private val db: AppDatabase,
    private val converter: PlaylistDbConverter,
    context: Context
) : PlaylistsRepository {

    override suspend fun addPlaylist(playlist: Playlist) {
        db.getPlaylistDao().insertPlaylist(converter.map(playlist))
    }

    override suspend fun deletePlaylisst(playlist: Playlist) {
        db.getPlaylistDao().deletePlaylist(converter.map(playlist))
    }

    override fun getPlaylists(): Flow<List<Playlist>> = flow {
        emit(
            db.getPlaylistDao().getPlaylists().map { playlistEntity ->
                converter.map(playlistEntity.apply { tracksCount = db.getRelativeDbDao().getPlaylistEntries(playlistEntity.id).size })
            }
        )
    }

    override suspend fun addTrackToPlaylist(track: Track, playlist: Playlist) : Boolean {
        val plid = playlist.id!!
        val result = db.getRelativeDbDao().addConnection(RelativeDbEntity(trackId = track.trackId, playlistId = plid))
        return result != -1L
    }

    override suspend fun deleteTrackFromPlaylist(track: Track, playlist: Playlist) {

    }

    override suspend fun getTrackIdList(playlist: Playlist): List<Int> {

        return listOf()
    }
}