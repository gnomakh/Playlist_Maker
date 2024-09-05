package com.practicum.playlistmaker

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.practicum.playlistmaker.network.*

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding

    private val tracks = ArrayList<Track>()
    private var historySearch = ArrayList<Track>()

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var prefConv: PrefGsonConvert
    private lateinit var sharedPrefListener: OnSharedPreferenceChangeListener

    private lateinit var adapter: TracksAdapter

    private val handler = Handler(Looper.getMainLooper())
    private val runnable = Runnable { searchRequest() }
    private var lastSearchQueue = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = LayoutInflater.from(this)
        binding = ActivitySearchBinding.inflate(inflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences(PrefGsonConvert.PREFS_KEY, MODE_PRIVATE)
        prefConv = PrefGsonConvert(sharedPreferences)

        val history = prefConv.getArrFromPref(PrefGsonConvert.HISTORY_KEY)
        if (history != null) {
            historySearch = history
        }

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.clearButton.setOnClickListener {
            binding.inputSearch.text.clear()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
            clearTrackList()
        }

        binding.updateButton.setOnClickListener {
            binding.progressBar.isVisible = true

            queue(adapter, trackService, tracks, lastSearchQueue)
        }

        binding.clearHistory.setOnClickListener {
            historySearch.clear()
            toggleOffHistory()
        }

        binding.inputSearch.doOnTextChanged { text, start, before, count ->
            binding.clearButton.isVisible = !(text.isNullOrEmpty())
        }

        binding.inputSearch.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus and binding.inputSearch.text.isEmpty()) {
                toggleOnHistory()
            } else {
                binding.youSearched.isVisible = false
                binding.clearHistory.isVisible = false
                adapter.trackList = tracks
                adapter.notifyDataSetChanged()
            }
        }

        binding.inputSearch.addTextChangedListener(
            beforeTextChanged = { text: CharSequence?, start: Int, count: Int, after: Int ->  },
            onTextChanged = { text: CharSequence?, start: Int, before: Int, count: Int ->
                debounceRequest()
                if (text.isNullOrEmpty() == true) clearTrackList()
                if (binding.inputSearch.hasFocus() && text?.isEmpty() == true) {
                    toggleOnHistory()
                } else {
                    toggleOffHistory()
                }
            },
            afterTextChanged = {text: Editable? ->  }
        )

        adapter = TracksAdapter(sharedPreferences)
        binding.rvTracks.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvTracks.adapter = adapter
        adapter.trackList = tracks

        sharedPrefListener = OnSharedPreferenceChangeListener { sharedPreferences, key ->
            if (key == PrefGsonConvert.TRACK_KEY) {
                val track = prefConv.getTrackFromPref()
                if (track != null) {
                    checkIfTrackIsThere(track)
                    if (historySearch.size > 10) {
                        historySearch.removeAt(10)
                    }
                    adapter.notifyDataSetChanged()
                }
            }
        }

        sharedPreferences.registerOnSharedPreferenceChangeListener(sharedPrefListener)
    }

    override fun onStop() {
        super.onStop()
        prefConv.saveArrToPref(historySearch, PrefGsonConvert.HISTORY_KEY)
    }

    fun queue(
        adapter: TracksAdapter,
        trackService: ITunesAPI,
        tracks: ArrayList<Track>,
        searchQueue: String
    ): Boolean {
        binding.searchPlaceholder.isVisible = false
        binding.networkErrorPalceholder.isVisible = false
        trackService.search(searchQueue)
            .enqueue(object : Callback<SearchResponse> {
                override fun onResponse(
                    call: Call<SearchResponse>, response: Response<SearchResponse>
                ) {
                    binding.progressBar.isVisible = false

                    when (response.code()) {
                        200 -> {
                            tracks.clear()
                            if (response.body()?.results?.isNotEmpty() == true) {
                                tracks.addAll(response.body()?.results!!)
                                toggleOffHistory()
                            } else {
                                adapter.notifyDataSetChanged()
                                binding.searchPlaceholder.isVisible = true
                            }
                        }

                        else -> {
                            clearTrackList()
                            binding.networkErrorPalceholder.isVisible = true
                        }
                    }
                }

                override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                    binding.progressBar.isVisible = false
                    clearTrackList()
                    binding.networkErrorPalceholder.isVisible = true
                }
            })
        return true
    }

    private fun searchRequest() {
        if(binding.inputSearch.text.isNullOrEmpty() == false) {
            lastSearchQueue = binding.inputSearch.text.toString()
            queue(adapter, trackService, tracks, lastSearchQueue)
            binding.progressBar.isVisible = true
        }
    }

    private fun toggleOnHistory() {
        if (historySearch.isEmpty() == false) {
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

    private fun checkIfTrackIsThere(track: Track) {
        if (historySearch.contains(track)) {
            historySearch.remove(track)
        }
        historySearch.add(0, track)
    }

    private fun clearTrackList() {
        tracks.clear()
        adapter.notifyDataSetChanged()
    }

    private fun debounceRequest() {
        handler.removeCallbacks(runnable)
        handler.postDelayed(runnable, SEARCH_DEBOUNCE_DELAY)
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}