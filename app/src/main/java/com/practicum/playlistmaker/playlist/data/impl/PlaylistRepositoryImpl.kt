package com.practicum.playlistmaker.playlist.data.impl

import android.util.Log
import com.practicum.playlistmaker.media.data.db.converters.PlaylistDbConverter
import com.practicum.playlistmaker.media.data.db.converters.TrackDbConverter
import com.practicum.playlistmaker.media.data.db.favorites_db.AppDatabase
import com.practicum.playlistmaker.media.domain.models.Playlist
import com.practicum.playlistmaker.playlist.domain.api.PlaylistRepository
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistRepositoryImpl(
    private val dataBase: AppDatabase,
    private val trackConverter: TrackDbConverter
) : PlaylistRepository {
    override suspend fun getTrackList(playlist: Playlist): Flow<List<Track>> = flow {
        val trackIds = dataBase.getRelativeDbDao().getPlaylistEntries(playlist.id!!)
        val trackList = dataBase.getTrackDao().getPlaylistTracks(trackIds)
        val trackListMapped = trackList.map { track ->
            trackConverter.map(track)
        }
        Log.i("playlist111", "playlistRepo ${trackList}")

        emit(trackListMapped)
    }

    override suspend fun deleteTrack(track: Track, playlistId: Int) {
        dataBase.getRelativeDbDao().deleteConnection(track.trackId, playlistId)
        val favoritesIds = dataBase.getTrackDao().getFavoritesId()
        if ((track.trackId in favoritesIds) or (dataBase.getRelativeDbDao()
                .hasRelationships(track.trackId))
        ) return
        dataBase.getTrackDao().deleteTrack(trackConverter.map(track))
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        dataBase.getRelativeDbDao().deletePlaylistRelations(playlist.id!!)
        dataBase.getPlaylistDao().deletePlaylist(playlist.id)
    }

    override suspend fun updatePlaylist(
        id: Int,
        title: String,
        description: String,
        picture: String?
    ) {
        dataBase.getPlaylistDao().updatePlaylist(id, title, description, picture)

    }

    override fun getShareMessage(
        tracks: List<Track>,
        playlist: Playlist,
        trackCount: String
    ): String {
        val playlistTitle = playlist.title
        val playlistDescription = playlist.description

        val tracksList = tracks.mapIndexed { index, track ->
            "${index + 1}. ${track.artistName} - ${track.trackName} (${track.trackTimeMillis})"
        }.joinToString("\n")

        return buildString {
            append(playlistTitle)
            if (!playlistDescription.isNullOrBlank()) {
                append("\n$playlistDescription")
            }
            append("\n$trackCount")
            append("\n$tracksList")
        }
    }
}