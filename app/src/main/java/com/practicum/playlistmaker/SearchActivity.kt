package com.practicum.playlistmaker

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
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
            binding.progressBar.visibility = View.VISIBLE
            queue(adapter, trackService, tracks, lastSearchQueue)
        }

        binding.clearHistory.setOnClickListener {
            historySearch.clear()
            toggleOffHistory()
        }

        binding.inputSearch.doOnTextChanged { text, start, before, count ->
            if (text.isNullOrEmpty()) {
                binding.clearButton.visibility = View.GONE
            } else {
                binding.clearButton.visibility = View.VISIBLE
            }
        }

        binding.inputSearch.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus and binding.inputSearch.text.isEmpty()) {
                toggleOnHistory()
            } else {
                binding.youSearched.visibility = View.GONE
                binding.clearHistory.visibility = View.GONE
                adapter.trackList = tracks
                adapter.notifyDataSetChanged()
            }
        }

        binding.inputSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                debounceRequest()
                if (p0.isNullOrEmpty() == true) clearTrackList()
                if (binding.inputSearch.hasFocus() && p0?.isEmpty() == true) {
                    toggleOnHistory()
                } else {
                    toggleOffHistory()
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

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
        binding.searchPlaceholder.visibility = View.GONE
        binding.networkErrorPalceholder.visibility = View.GONE
        trackService.search(searchQueue)
            .enqueue(object : Callback<SearchResponse> {
                override fun onResponse(
                    call: Call<SearchResponse>, response: Response<SearchResponse>
                ) {
                    binding.progressBar.visibility = View.GONE

                    when (response.code()) {
                        200 -> {
                            tracks.clear()
                            if (response.body()?.results?.isNotEmpty() == true) {
                                tracks.addAll(response.body()?.results!!)
                                toggleOffHistory()
                            } else {
                                adapter.notifyDataSetChanged()
                                binding.searchPlaceholder.visibility = View.VISIBLE
                            }
                        }

                        else -> {
                            clearTrackList()
                            binding.networkErrorPalceholder.visibility = View.VISIBLE
                        }
                    }
                }

                override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                    binding.progressBar.visibility = View.GONE
                    clearTrackList()
                    binding.networkErrorPalceholder.visibility = View.VISIBLE
                }
            })
        return true
    }

    private fun searchRequest() {
        if(binding.inputSearch.text.isNullOrEmpty() == false) {
            lastSearchQueue = binding.inputSearch.text.toString()
            queue(adapter, trackService, tracks, lastSearchQueue)
            binding.progressBar.visibility = View.VISIBLE
        }
    }

    private fun toggleOnHistory() {
        if (historySearch.isEmpty() == false) {
            binding.youSearched.visibility = View.VISIBLE
            binding.clearHistory.visibility = View.VISIBLE
            adapter.trackList = historySearch
            adapter.notifyDataSetChanged()
        }
    }

    private fun toggleOffHistory() {
        binding.youSearched.visibility = View.GONE
        binding.clearHistory.visibility = View.GONE
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