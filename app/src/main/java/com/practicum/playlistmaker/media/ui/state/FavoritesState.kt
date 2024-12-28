package com.practicum.playlistmaker.media.ui.state

import com.practicum.playlistmaker.media.domain.api.FavoritesInteractor
import com.practicum.playlistmaker.search.domain.models.Track

sealed class FavoritesState {
    data class Content(val favorites: List<Track>) : FavoritesState()
    object Empty : FavoritesState()

}