package com.practicum.playlistmaker.search.ui.ViewModel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.search.domain.api.HistoryInteractor
import com.practicum.playlistmaker.search.domain.consumer.TrackConsumer
import com.practicum.playlistmaker.search.domain.impl.GetTracksUseCase
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.ui.state.SearchScreenState

class SearchViewModel(
    val getTracksUseCase: GetTracksUseCase,
    val getHistoryInteractor: HistoryInteractor): ViewModel() {

    private var lastSearchQueue = ""
    private var currentSearchQueue = ""
    private var networkFailed = false
    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { searchRequest() }

    private val searchLiveData = MutableLiveData<ArrayList<Track>>()
    fun getSearchLiveData() : LiveData<ArrayList<Track>> = searchLiveData

    private val historyLiveData = MutableLiveData<ArrayList<Track>>()
    fun getHistoryLiveData() : LiveData<ArrayList<Track>> = historyLiveData

    private val screenStateLiveData = MutableLiveData<SearchScreenState>()
    fun getScreenStateLiveData() : LiveData<SearchScreenState> = screenStateLiveData

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
        currentSearchQueue = searchInput
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    fun removeCallbacks() {
        handler.removeCallbacks(searchRunnable)
    }

    private fun searchRequest() {
        if(networkFailed == false) lastSearchQueue = currentSearchQueue
        screenStateLiveData.setValue(SearchScreenState.Loading)
        networkFailed = false
        getTracksUseCase.execute(lastSearchQueue, object : TrackConsumer {
            override fun onSuccess(response: ArrayList<Track>) {
                searchLiveData.postValue(response)
                screenStateLiveData.postValue(SearchScreenState.Tracks)
            }

            override fun onNoResult() {
                screenStateLiveData.postValue(SearchScreenState.EmptyResult)
            }

            override fun onNetworkError() {
                networkFailed = true
                screenStateLiveData.postValue(SearchScreenState.NetwotkError)
            }
        })
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

}