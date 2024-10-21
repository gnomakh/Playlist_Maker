package com.practicum.playlistmaker.data.repository_impl

import android.annotation.SuppressLint
import com.practicum.playlistmaker.data.NetworkClient
import com.practicum.playlistmaker.data.dto.Response
import com.practicum.playlistmaker.data.dto.SearchRequest
import com.practicum.playlistmaker.data.dto.SearchResponse
import com.practicum.playlistmaker.domain.repository.TrackRepository
import com.practicum.playlistmaker.domain.models.Track

class TrackRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {
    override var resultCode = 0

    @SuppressLint("SuspiciousIndentation")
    override fun searchTracks(expression: String) : ArrayList<Track>{
        var response: Response = networkClient.doRequest(SearchRequest(expression))
        resultCode = response.resultCode
        if (resultCode == 200) {
            return ArrayList((response as SearchResponse).results.map {
                Track(
                    it.trackName,
                    it.artistName,
                    it.trackTimeMillis,
                    it.artworkUrl100,
                    it.trackId,
                    it.collectionName,
                    it.releaseDate,
                    it.primaryGenreName,
                    it.country,
                    it.previewUrl
                )
            })
        } else return arrayListOf()
    }
}