package com.practicum.playlistmaker.search.ui.ViewModel

import android.app.Application
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.search.domain.consumer.TrackConsumer
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.ui.state.SearchScreenState

class SearchViewModel(application: Application): AndroidViewModel(application) {
    private val getTracksUseCase = Creator.provideGetTracksUseCase()
    private val getHistoryInteractor = Creator.provideHistoryInteractor(getApplication())

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
        historyLiveData.postValue(getHistoryInteractor.getHistory())
    }

    fun postState(state: SearchScreenState) {
        screenStateLiveData.postValue(state)
    }

    fun clearHistory() {
        historyLiveData.postValue(arrayListOf())
    }

    fun addTrackToHistory(track: Track) {
        getHistoryInteractor.addtrackToHistory(track)
        historyLiveData.postValue(getHistoryInteractor.getHistory())
    }

    fun clearTrackList() {
        searchLiveData.postValue(arrayListOf())
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
        screenStateLiveData.postValue(SearchScreenState.Loading)
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