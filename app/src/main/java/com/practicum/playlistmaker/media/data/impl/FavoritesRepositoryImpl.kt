package com.practicum.playlistmaker.media.data.impl

import com.practicum.playlistmaker.media.data.db.favorites_db.AppDatabase
import com.practicum.playlistmaker.media.data.db.entities.TrackEntity
import com.practicum.playlistmaker.media.data.db.converters.TrackDbConverter
import com.practicum.playlistmaker.media.domain.api.FavoritesRepository
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoritesRepositoryImpl(
    private val appDataBase: AppDatabase,
    private val converter: TrackDbConverter
) : FavoritesRepository {
    override suspend fun addTrack(track: Track) {
        appDataBase.getTrackDao().insertTrack(convert(track))
    }

    override suspend fun deleteTrack(track: Track) {
        appDataBase.getTrackDao().deleteTrack(convert(track))
    }

    override fun getFavorites(): Flow<List<Track>> = flow {
        val tracks = appDataBase.getTrackDao().getTrackList()
        emit(convert(tracks))
    }

    override suspend fun isFavorite(trackId: Int) : Boolean {
        return trackId in appDataBase.getTrackDao().getFavoritesId()
    }

    private fun convert(tracks: List<TrackEntity>): List<Track> {
        return tracks.map {
            track -> converter.map(track)
        }
    }

    private fun convert(track: Track) : TrackEntity {
        return converter.map(track)
    }

}