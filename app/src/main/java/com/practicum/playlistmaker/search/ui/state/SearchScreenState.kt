package com.practicum.playlistmaker.search.ui.state

import com.practicum.playlistmaker.search.domain.models.Track

sealed class SearchScreenState {
    object Loading: SearchScreenState()
    object Tracks: SearchScreenState()
    object History: SearchScreenState()
    object NetwotkError: SearchScreenState()
    object EmptyResult: SearchScreenState()
    object Nothing : SearchScreenState()
}