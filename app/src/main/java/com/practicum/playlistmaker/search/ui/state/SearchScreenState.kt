package com.practicum.playlistmaker.search.ui.state

import com.practicum.playlistmaker.search.domain.models.Track

sealed class SearchScreenState {
    object Loading : SearchScreenState()
    data class Tracks(val tracksSearch: List<Track>) : SearchScreenState()
    data class History(val historyList: List<Track>) : SearchScreenState()
    object NetwotkError : SearchScreenState()
    object EmptyResult : SearchScreenState()
    object Nothing : SearchScreenState()
}