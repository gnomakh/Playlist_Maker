package com.practicum.playlistmaker.data.network

import com.practicum.playlistmaker.data.dto.SearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesAPI {
    @GET("/search?entity=song")
    fun searchTracks(@Query("term") text: String): Call<SearchResponse>
}
