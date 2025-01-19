package com.practicum.playlistmaker.media.data.db.playlists_db

import androidx.core.net.toUri
import com.practicum.playlistmaker.media.domain.models.Playlist

class PlaylistDbConverter {
    fun map(playlistEntity: PlaylistEntity) : Playlist {
        return Playlist(
            title = playlistEntity.title,
            description = playlistEntity.description,
            picture = playlistEntity.picture,
            tracksCount = playlistEntity.tracksCount
        )
    }

    fun map(playlist: Playlist) : PlaylistEntity {
        return PlaylistEntity(
            title = playlist.title,
            description = playlist.description,
            picture = playlist.picture,
            tracksCount = playlist.tracksCount
        )
    }
}