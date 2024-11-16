package com.practicum.playlistmaker.search.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.search.domain.consumer.TrackConsumer
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.player.ui.PlayerActivity
import com.practicum.playlistmaker.search.ui.ViewModel.SearchViewModel
import com.practicum.playlistmaker.search.ui.track.TracksAdapter

class SearchActivity : ComponentActivity() {

    private lateinit var binding: ActivitySearchBinding

    private val tracks = arrayListOf<Track>()
    private lateinit var historySearch: ArrayList<Track>
    private lateinit var adapter: TracksAdapter
    private val getTracksUseCase = Creator.provideGetTracksUseCase()
    private val getHistoryInteractor = Creator.provideHistoryInteractor()

    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { searchRequest() }

    private var clickCurrentState = true
    private var lastSearchQueue = ""
    private var networkFailed = false

    private val viewModel by lazy {
        ViewModelProvider(this)[SearchViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = LayoutInflater.from(this)
        binding = ActivitySearchBinding.inflate(inflater)
        setContentView(binding.root)

        historySearch = getHistoryInteractor.getHistory()

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.updateButton.setOnClickListener {
            binding.progressBar.isVisible = true
            debounceRequest()
        }

        binding.clearHistory.setOnClickListener {
            historySearch.clear()
            getHistoryInteractor.clearHistory()
            toggleOffHistory()
        }

        binding.clearButton.setOnClickListener {
            binding.inputSearch.text.clear()
            clearTrackList()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }

        binding.inputSearch.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus and binding.inputSearch.text.isEmpty()) {
                toggleOnHistory()
            } else {
                toggleOffHistory()
            }
        }

        binding.inputSearch.doOnTextChanged { text, start, before, count ->
            binding.clearButton.isVisible = !(text.isNullOrEmpty())
        }

        binding.inputSearch.addTextChangedListener(
            beforeTextChanged = { text: CharSequence?, start: Int, count: Int, after: Int -> },
            onTextChanged = { text: CharSequence?, start: Int, before: Int, count: Int ->
                if (!text.isNullOrEmpty()) {
                    if(networkFailed == false) lastSearchQueue = text.toString()
                    debounceRequest()
                } else {
                    clearTrackList()
                    handler.removeCallbacks(searchRunnable)
                }
                if (binding.inputSearch.hasFocus() && text?.isEmpty() == true) {
                    toggleOnHistory()
                } else {
                    toggleOffHistory()
                }
            },
            afterTextChanged = { text: Editable? -> }
        )

        adapter = TracksAdapter()
        adapter.listener = TracksAdapter.OnTrackClickListener { track ->
            if(debounceClick()) {
                historySearch = getHistoryInteractor.addtrackToHistory(historySearch, track)
                getHistoryInteractor.saveHistory(historySearch)
                adapter.notifyDataSetChanged()
                val playerIntent = Intent(this, PlayerActivity::class.java)
                startActivity(playerIntent)
            }
        }
        adapter.trackList = tracks

        binding.rvTracks.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvTracks.adapter = adapter
    }

    private fun searchRequest() {
        networkFailed = false
        toggleOffPlaceholders()
        binding.progressBar.isVisible = true
        getTracksUseCase.execute(lastSearchQueue, object : TrackConsumer {
            override fun onSuccess(response: ArrayList<Track>) {
                runOnUiThread {
                    showTracks(response)
                }
            }

            override fun onNoResult() {
                runOnUiThread {
                    binding.rvTracks.isVisible = false
                    showErrorPlaceholder(ResponseCode.NO_RESULT)
                }
            }

            override fun onNetworkError() {
                networkFailed = true
                runOnUiThread {
                    showErrorPlaceholder(ResponseCode.NETWORK_ERROR)
                }
            }
        })
    }

    fun showTracks(trackList: ArrayList<Track>) {
        toggleOffPlaceholders()
        binding.progressBar.isVisible = false
        binding.rvTracks.isVisible = true
        adapter.trackList = trackList
        adapter.notifyDataSetChanged()
    }

    fun showErrorPlaceholder(errorCode: ResponseCode) {
        toggleOffHistory()
        binding.progressBar.isVisible = false
        when(errorCode) {
            ResponseCode.NO_RESULT -> {
                binding.searchPlaceholder.isVisible = true
            }
            ResponseCode.NETWORK_ERROR -> {
                binding.networkErrorPalceholder.isVisible = true
            }
        }
    }

    fun toggleOffPlaceholders() {
        binding.networkErrorPalceholder.isVisible = false
        binding.searchPlaceholder.isVisible = false
    }

    private fun toggleOnHistory() {
        toggleOffPlaceholders()
        if (historySearch.isNotEmpty()) {
            binding.rvTracks.isVisible = true
            binding.youSearched.isVisible = true
            binding.clearHistory.isVisible = true
            adapter.trackList = historySearch
            adapter.notifyDataSetChanged()
        }
    }

    private fun toggleOffHistory() {
        binding.youSearched.isVisible = false
        binding.clearHistory.isVisible = false
        adapter.trackList = tracks
        adapter.notifyDataSetChanged()
    }

    private fun clearTrackList() {
        tracks.clear()
        adapter.notifyDataSetChanged()
    }

    private fun debounceRequest() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun debounceClick(): Boolean {
        var isClickAllowed = clickCurrentState
        if (clickCurrentState) {
            clickCurrentState = false
            handler.postDelayed({ clickCurrentState = true }, CLICK_DEBOUNCE_DELAY)
        }
        return isClickAllowed
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    enum class ResponseCode{NO_RESULT, NETWORK_ERROR}
}