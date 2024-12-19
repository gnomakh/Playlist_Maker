package com.practicum.playlistmaker.search.data.network

import com.practicum.playlistmaker.search.data.dto.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesAPI {
    @GET("/search?entity=song")
    suspend fun searchTracks(@Query("term") text: String): SearchResponse
}
