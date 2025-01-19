package com.practicum.playlistmaker.media.domain.models

import android.net.Uri

data class Playlist(
    val title: String,
    val description: String,
    val picture: String,
    var tracksCount: String
)