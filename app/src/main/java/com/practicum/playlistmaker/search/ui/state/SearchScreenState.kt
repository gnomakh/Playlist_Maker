package com.practicum.playlistmaker.search.ui.state

sealed class SearchScreenState {
    object Loading : SearchScreenState()
    object Tracks : SearchScreenState()
    object History : SearchScreenState()
    object NetwotkError : SearchScreenState()
    object EmptyResult : SearchScreenState()
    object Nothing : SearchScreenState()
}