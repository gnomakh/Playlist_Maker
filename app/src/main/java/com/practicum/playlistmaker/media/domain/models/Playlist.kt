package com.practicum.playlistmaker.media.domain.models

data class Playlist(
    val id: Int? = null,
    val title: String,
    val description: String,
    val picture: String?,
    var tracksCount: Int = 0
)