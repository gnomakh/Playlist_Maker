package com.practicum.playlistmaker.ui.tracks_search

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker.PrefGsonConvert
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.domain.consumer.TrackConsumer
import com.practicum.playlistmaker.domain.models.EditTextState
import com.practicum.playlistmaker.domain.models.Track

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding

    private val tracks = ArrayList<Track>()
    private var historySearch = ArrayList<Track>()
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var prefConv: PrefGsonConvert
    private lateinit var sharedPrefListener: OnSharedPreferenceChangeListener
    private lateinit var adapter: TracksAdapter
    private val getTracksUseCase = Creator.provideGetTracksUseCase()
    private val trackListInteractor = Creator.provideTrackListInteractor(sharedPreferences)

    private val handler = Handler(Looper.getMainLooper())
    private var lastSearchQueue = ""
    private val searchRunnable = Runnable { searchRequest(lastSearchQueue) }

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
            binding.inputSearch.text.append(EditTextState.editTextState)
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }

        binding.updateButton.setOnClickListener {
            binding.progressBar.isVisible = true
        }

        binding.clearHistory.setOnClickListener {
            historySearch.clear()
            toggleOffHistory()
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

        binding.inputSearch.doOnTextChanged { text, start, before, count ->
            binding.clearButton.isVisible = !(text.isNullOrEmpty())
        }

        binding.inputSearch.addTextChangedListener(
            beforeTextChanged = { text: CharSequence?, start: Int, count: Int, after: Int -> },
            onTextChanged = { text: CharSequence?, start: Int, before: Int, count: Int ->
                debounceRequest()
                if (text.isNullOrEmpty()) clearTrackList()
                if (binding.inputSearch.hasFocus() && text?.isEmpty() == true) {
                    toggleOnHistory()
                } else {
                    toggleOffHistory()
                }
            },
            afterTextChanged = { text: Editable? -> }
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

    private fun searchRequest(expression: String) {
        lastSearchQueue = expression
        binding.progressBar.isVisible = true
        getTracksUseCase.execute(binding.inputSearch.text.toString(), object : TrackConsumer {
            override fun onSuccess(response: ArrayList<Track>) {
                runOnUiThread {
                    showTracks(response)
                }
            }

            override fun onFailure(resultCode: Int) {
                runOnUiThread {
                    binding.progressBar.isVisible = false
                    binding.rvTracks.isVisible = false
                    Toast.makeText(this@SearchActivity, "$resultCode", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    fun showTracks(trackList: ArrayList<Track>) {
        binding.progressBar.isVisible = false
        binding.rvTracks.isVisible = true
        adapter.trackList = trackList
        adapter.notifyDataSetChanged()
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
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun debounceClick(): Boolean {
        var isClickAllowed = true
        if (isClickAllowed) {
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return isClickAllowed
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}