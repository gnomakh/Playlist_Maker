package com.practicum.playlistmaker.data.network

import com.practicum.playlistmaker.data.NetworkClient
import com.practicum.playlistmaker.data.dto.Response
import com.practicum.playlistmaker.data.dto.SearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



class RetrofitNetworkClient : NetworkClient {

    private val tracksBaseUrl = "https://itunes.apple.com"

    private val retrofit =
        Retrofit.Builder()
            .baseUrl(tracksBaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    val trackService = retrofit.create(ITunesAPI::class.java)

    override fun doRequest(dto: Any): Response {
        if (dto is SearchRequest) {
            val resp = trackService.searchTracks(dto.expression).execute()

            val body = resp.body() ?: Response()

            return body.apply { resultCode = resp.code() }
        } else {
            return Response().apply { resultCode = 400 }
        }
    }
}