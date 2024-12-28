package com.practicum.playlistmaker.media.data.impl

import com.example.courutines.db.AppDatabase
import com.example.courutines.db.TrackEntity
import com.practicum.playlistmaker.media.data.db.TrackDbConverter
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

    private fun convert(tracks: List<TrackEntity>): List<Track> {
        return tracks.map {
            track -> converter.map(track)
        }
    }

    private fun convert(track: Track) : TrackEntity {
        return converter.map(track)
    }

}