package com.practicum.playlistmaker.data.mapper

import com.practicum.playlistmaker.domain.models.Track

class TrackMapper {
    fun map (track : Track) : Track {
        return Track(trackName = track.trackName,
        artistName = track.artistName,
        trackTimeMillis = track.trackTimeMillis,
        artworkUrl100 = track.artworkUrl100,
        trackId = track.trackId,
        collectionName = track.collectionName,
        releaseDate = track.releaseDate,
        primaryGenreName = track.primaryGenreName,
        country = track.country,
        previewUrl = track.previewUrl
        )
    }
}