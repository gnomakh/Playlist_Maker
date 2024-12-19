package com.practicum.playlistmaker.search.ui.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.search.domain.api.HistoryInteractor
import com.practicum.playlistmaker.search.domain.impl.GetTracksUseCase
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.ui.state.SearchScreenState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(
    private val getTracksUseCase: GetTracksUseCase,
    private val getHistoryInteractor: HistoryInteractor
) : ViewModel() {

    private var lastSearchQueue = ""
    private var currentSearchQueue = ""
    private var searchJob: Job? = null
    private var networkFailed = false

    private var currentTrackList: List<Track> = arrayListOf()

    private val screenStateLiveData = MutableLiveData<SearchScreenState>()
    fun getScreenStateLiveData(): LiveData<SearchScreenState> = screenStateLiveData

    fun renderTracks() {
        screenStateLiveData.postValue(SearchScreenState.Tracks(currentTrackList))
    }

    fun renderHistory() {
        if(getHistoryInteractor.getHistory().isEmpty()) {
            screenStateLiveData.postValue(SearchScreenState.Nothing)
            return
        }
        screenStateLiveData.postValue(SearchScreenState.History(getHistoryInteractor.getHistory()))
    }

    fun clearHistory() {
        getHistoryInteractor.clearHistory()
    }

    fun addTrackToHistory(track: Track) {
        getHistoryInteractor.addtrackToHistory(track)
    }

    fun clearTrackList() {
        currentTrackList = arrayListOf()
    }

    fun cancelJob() {
        searchJob?.cancel()
    }

    fun retrySearch() {
        debounceRequest(lastSearchQueue)
    }

    fun debounceRequest(searchInput: String) {
        searchJob?.cancel()
        if ((currentSearchQueue == searchInput) and (networkFailed == false)) return
        currentSearchQueue = searchInput
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            searchRequest()
        }
    }

    private fun searchRequest() {
        if (!networkFailed) lastSearchQueue = currentSearchQueue
        screenStateLiveData.setValue(SearchScreenState.Loading)
        networkFailed = false
        viewModelScope.launch {
            getTracksUseCase.searchTracks(lastSearchQueue)
                .collect { pair ->
                    processResult(pair.first, pair.second)
                }
        }
    }

    private fun processResult(tracks: List<Track>?, message: String?) {
        when {
            !tracks.isNullOrEmpty() -> {
                currentTrackList = tracks
                screenStateLiveData.postValue(SearchScreenState.Tracks(currentTrackList))
            }

            tracks.isNullOrEmpty() and message.isNullOrEmpty() -> {
                screenStateLiveData.postValue(SearchScreenState.EmptyResult)
            }

            else -> {
                networkFailed = true
                screenStateLiveData.postValue(SearchScreenState.NetwotkError)
            }
        }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}