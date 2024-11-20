package com.practicum.playlistmaker.search.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.player.ui.PlayerActivity
import com.practicum.playlistmaker.search.ui.ViewModel.SearchViewModel
import com.practicum.playlistmaker.search.ui.state.SearchScreenState
import com.practicum.playlistmaker.search.ui.track.TracksAdapter

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding

    private var tracks = arrayListOf<Track>()
    private var historySearch: ArrayList<Track> = arrayListOf()
    private val adapter: TracksAdapter = TracksAdapter()

    private val handler = Handler(Looper.getMainLooper())

    private var clickCurrentState = true
    private var searchQueue = ""

    private val viewModel by lazy {
        ViewModelProvider(this)[SearchViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        val inflater = LayoutInflater.from(this)
        binding = ActivitySearchBinding.inflate(inflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.updateButton.setOnClickListener {
            binding.progressBar.isVisible = true
            viewModel.debounceRequest(searchQueue)
        }

        binding.clearHistory.setOnClickListener {
            historySearch.clear()
            viewModel.clearHistory()
            toggleOffHistory()
        }

        viewModel.getHistoryLiveData().observe(this) {
            historySearch = it
        }

        viewModel.getSearchLiveData().observe(this) {
            tracks = it
        }

        viewModel.getScreenStateLiveData().observe(this) {
            render(it)
        }

        binding.clearButton.setOnClickListener {
            binding.inputSearch.text.clear()
            clearTrackList()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }

        binding.inputSearch.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus and binding.inputSearch.text.isEmpty()) {
                viewModel.postState(SearchScreenState.History)
            } else {
                viewModel.postState(SearchScreenState.Nothing)
            }
        }

        binding.inputSearch.doOnTextChanged { text, start, before, count ->
            binding.clearButton.isVisible = !(text.isNullOrEmpty())
        }

        binding.inputSearch.addTextChangedListener(
            beforeTextChanged = { text: CharSequence?, start: Int, count: Int, after: Int -> },
            onTextChanged = { text: CharSequence?, start: Int, before: Int, count: Int ->
                if (!text.isNullOrEmpty()) {
                    searchQueue = text.toString()
                    viewModel.debounceRequest(searchQueue)
                } else {
                    clearTrackList()
                    viewModel.removeCallbacks()
                }
                if (binding.inputSearch.hasFocus() && text?.isEmpty() == true) {
                    viewModel.postState(SearchScreenState.History)
                } else {
                    viewModel.postState(SearchScreenState.Nothing)
                }
            },
            afterTextChanged = { text: Editable? -> }
        )

        adapter.listener = TracksAdapter.OnTrackClickListener { track ->
            if(debounceClick()) {
                viewModel.addTrackToHistory(track)
                render(SearchScreenState.History)
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

    private fun render(state: SearchScreenState) {
        when(state) {
            SearchScreenState.Loading -> showLoading()
            SearchScreenState.Tracks -> showTracks()
            SearchScreenState.History -> toggleOnHistory()
            SearchScreenState.EmptyResult -> showErrorPlaceholder(ResponseCode.NO_RESULT)
            SearchScreenState.NetwotkError -> showErrorPlaceholder(ResponseCode.NETWORK_ERROR)
            SearchScreenState.Nothing -> {
                hideLoading()
                toggleOffHistory()
                toggleOffPlaceholders()
                viewModel.clearTrackList()
            }
        }
    }

    private fun showTracks() {
        toggleOffPlaceholders()
        toggleOffHistory()
        hideLoading()
        binding.rvTracks.isVisible = true
        adapter.trackList = tracks
        adapter.notifyDataSetChanged()
    }

    private fun showErrorPlaceholder(errorCode: ResponseCode) {
        toggleOffHistory()
        hideLoading()
        when(errorCode) {
            ResponseCode.NO_RESULT -> {
                binding.searchPlaceholder.isVisible = true
            }
            ResponseCode.NETWORK_ERROR -> {
                binding.networkErrorPalceholder.isVisible = true
            }
        }
    }

    private fun toggleOffPlaceholders() {
        binding.networkErrorPalceholder.isVisible = false
        binding.searchPlaceholder.isVisible = false
    }

    private fun toggleOnHistory() {
        toggleOffPlaceholders()
        hideLoading()
        historySearch = viewModel.getHistoryLiveData().value ?: arrayListOf()
        if (historySearch.isNotEmpty() and tracks.isEmpty()) {
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
        viewModel.clearTrackList()
        adapter.notifyDataSetChanged()
    }


    private fun showLoading() {
        binding.progressBar.isVisible = true
        toggleOffHistory()
        toggleOffPlaceholders()
        viewModel.clearTrackList()
    }

    private fun hideLoading() {
        binding.progressBar.isVisible = false
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
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    enum class ResponseCode{NO_RESULT, NETWORK_ERROR}
}