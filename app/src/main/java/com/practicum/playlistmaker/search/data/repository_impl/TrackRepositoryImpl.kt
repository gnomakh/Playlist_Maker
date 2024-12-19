package com.practicum.playlistmaker.search.data.repository_impl

import com.practicum.playlistmaker.search.data.NetworkClient
import com.practicum.playlistmaker.search.data.dto.SearchRequest
import com.practicum.playlistmaker.search.data.dto.SearchResponse
import com.practicum.playlistmaker.search.domain.api.TrackRepository
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import java.util.Locale

class TrackRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {

    override fun searchTracks(expression: String): Flow<Pair<List<Track>?, String?>> = flow {
        var response = networkClient.doRequest(SearchRequest(expression))

        if (response.resultCode == -1) {
            emit(Pair(null, "Network Error"))
            return@flow
        }

        if (response.resultCode == 200) {
            val result = ((response as SearchResponse).results.map {
                Track(
                    it.trackName,
                    it.artistName,
                    SimpleDateFormat("mm:ss", Locale.getDefault()).format(it.trackTimeMillis),
                    it.artworkUrl100,
                    it.trackId,
                    it.collectionName,
                    it.releaseDate,
                    it.primaryGenreName,
                    it.country,
                    it.previewUrl
                )
            }) as ArrayList<Track>
            emit(Pair(result, null))
        } else emit(Pair(null, null))
    }
}