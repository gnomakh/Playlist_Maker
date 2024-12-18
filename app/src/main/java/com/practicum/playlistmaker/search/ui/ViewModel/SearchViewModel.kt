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
    val getTracksUseCase: GetTracksUseCase,
    val getHistoryInteractor: HistoryInteractor
) : ViewModel() {

    private var lastSearchQueue = ""
    private var currentSearchQueue = ""
    private var searchJob: Job? = null
    private var networkFailed = false

    private val searchLiveData = MutableLiveData<ArrayList<Track>>()
    fun getSearchLiveData(): LiveData<ArrayList<Track>> = searchLiveData

    private val historyLiveData = MutableLiveData<ArrayList<Track>>()
    fun getHistoryLiveData(): LiveData<ArrayList<Track>> = historyLiveData

    private val screenStateLiveData = MutableLiveData<SearchScreenState>()
    fun getScreenStateLiveData(): LiveData<SearchScreenState> = screenStateLiveData

    init {
        historyLiveData.setValue(getHistoryInteractor.getHistory())
    }

    fun postState(state: SearchScreenState) {
        screenStateLiveData.setValue(state)
    }

    fun clearHistory() {
        getHistoryInteractor.clearHistory()
        historyLiveData.setValue(arrayListOf())
    }

    fun addTrackToHistory(track: Track) {
        val historyList = historyLiveData.value ?: arrayListOf()
        getHistoryInteractor.addtrackToHistory(historyList, track)
        historyLiveData.setValue(getHistoryInteractor.getHistory())
    }

    fun clearTrackList() {
        searchLiveData.setValue(arrayListOf())
    }

    fun debounceRequest(searchInput: String) {
        if (currentSearchQueue == searchInput) return

        searchJob?.cancel()
        currentSearchQueue = searchInput
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            searchRequest()
        }
    }

    private fun searchRequest() {
        if (networkFailed == false) lastSearchQueue = currentSearchQueue
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
        if (!tracks.isNullOrEmpty()) {
            searchLiveData.postValue(tracks as ArrayList<Track>)
            screenStateLiveData.postValue(SearchScreenState.Tracks)
        } else if (tracks.isNullOrEmpty() and message.isNullOrEmpty()) {
            screenStateLiveData.postValue(SearchScreenState.EmptyResult)
        } else {
            networkFailed = true
            screenStateLiveData.postValue(SearchScreenState.NetwotkError)
        }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}