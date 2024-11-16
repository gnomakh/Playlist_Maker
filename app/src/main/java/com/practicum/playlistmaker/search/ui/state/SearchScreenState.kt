package com.practicum.playlistmaker.search.ui.state

import com.practicum.playlistmaker.search.domain.models.Track

sealed class SearchScreenState {
    object Loading: SearchScreenState()
    data class Content(
        val trackModel: Track,
    ): SearchScreenState()
    object NetwotkError: SearchScreenState()
    object EmptyResult: SearchScreenState()
}