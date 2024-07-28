package com.practicum.playlistmaker.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private val tracksBaseUrl = "https://itunes.apple.com"

private val retrofit =
    Retrofit.Builder()
        .baseUrl(tracksBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

val trackService = retrofit.create(ITunesAPI::class.java)