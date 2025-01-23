package com.practicum.playlistmaker.media.data.db.converters

import com.practicum.playlistmaker.media.data.db.entities.PlaylistEntity
import com.practicum.playlistmaker.media.domain.models.Playlist

class PlaylistDbConverter {
    fun map(playlistEntity: PlaylistEntity) : Playlist {
        return Playlist(
            id = playlistEntity.id,
            title = playlistEntity.title,
            description = playlistEntity.description,
            picture = playlistEntity.picture,
            tracksCount = playlistEntity.tracksCount
        )
    }

    fun map(playlist: Playlist) : PlaylistEntity {
        if(playlist.id != null) return PlaylistEntity(
            id = playlist.id,
            title = playlist.title,
            description = playlist.description,
            picture = playlist.picture,
            tracksCount = playlist.tracksCount
        )
        return PlaylistEntity(
            title = playlist.title,
            description = playlist.description,
            picture = playlist.picture,
            tracksCount = playlist.tracksCount
        )
    }
}