package com.practicum.playlistmaker

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.practicum.playlistmaker.network.*

class SearchActivity : AppCompatActivity() {
    private var editTextState: CharSequence? = DEFAULT_STATE
    private var tracksState: ArrayList<Track> = DEFAULT_TRACKS_STATE

    private lateinit var binding: ActivitySearchBinding

    private lateinit var backButton: ImageView
    private lateinit var updateButton: Button
    private lateinit var inputEditText: EditText
    private lateinit var clearButton: ImageView
    private lateinit var rvTrackList: RecyclerView

    private val tracks = ArrayList<Track>()
    private var historySearch = ArrayList<Track>()

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var prefConv: PrefGsonConvert
    private lateinit var sharedPrefListener: OnSharedPreferenceChangeListener

    private lateinit var adapter: TracksAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = LayoutInflater.from(this)
        binding = ActivitySearchBinding.inflate(inflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences(PREFS_KEY, MODE_PRIVATE)
        prefConv = PrefGsonConvert(sharedPreferences)

        backButton = binding.backButton
        updateButton = binding.updateButton
        inputEditText = binding.inputSearch
        clearButton = binding.clearButton

        backButton.setOnClickListener {
            finish()
        }

        clearButton.setOnClickListener {
            inputEditText.setText("")
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
            clearTrackList()
            toggleSearchHistory()
        }

        var lastSearchQueue = ""

        binding.updateButton.setOnClickListener {
            queue(adapter, trackService, tracks, lastSearchQueue)
        }

        binding.clearHistory.setOnClickListener {
            historySearch.clear()
            toggleSearchHistory()
        }

        binding.youSearched.setOnClickListener {
            historySearch.clear()
            toggleSearchHistory()
        }

        inputEditText.doOnTextChanged { text, start, before, count ->
            if (text.isNullOrEmpty()) {
                clearButton.visibility = View.GONE
            } else {
                clearButton.visibility = View.VISIBLE
            }
        }

        inputEditText.setOnFocusChangeListener { view, hasFocus ->

        }

        adapter = TracksAdapter(sharedPreferences)
        rvTrackList = binding.rvTracks
        rvTrackList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvTrackList.adapter = adapter
        adapter.trackList = tracks



        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                lastSearchQueue = binding.inputSearch.text.toString()
                queue(adapter, trackService, tracks, lastSearchQueue)
            }
            false
        }

        tracks.clear()
        val history = prefConv.getArrFromPref(HISTORY_KEY)
        if(history != null) {
            historySearch = history
            toggleSearchHistory()
        }

        toggleSearchHistory()


        sharedPrefListener = OnSharedPreferenceChangeListener {sharedPreferences, key ->
            if(key == TRACK_KEY) {
                val track = prefConv.getTrackFromPref()
                if(track != null) {
                    checkIfTrackIsThere(track)
                    if(historySearch.size > 10) {
                        historySearch.removeAt(10)
                    }
                    checkIfTrackIsThere(track)
                    Toast.makeText(this,"Трек ${track.trackName} добавлен", Toast.LENGTH_SHORT).show()
                }
            }
        }

        sharedPreferences.registerOnSharedPreferenceChangeListener(sharedPrefListener)
    }

    override fun onStop() {
        super.onStop()
        prefConv.saveArrToPref(historySearch, HISTORY_KEY)
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

                    when (response.code()) {
                        200 -> {
                            tracks.clear()
                            if (response.body()?.results?.isNotEmpty() == true) {
                                tracks.addAll(response.body()?.results!!)
                                toggleSearchHistory()
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
                    clearTrackList()
                    binding.networkErrorPalceholder.visibility = View.VISIBLE
                }
            })
        return true
    }

    private fun toggleSearchHistory() {
        if(!tracks.isEmpty() or historySearch.isEmpty()) {
            binding.youSearched.visibility = View.GONE
            binding.clearHistory.visibility = View.GONE
            adapter.trackList = tracks
            adapter.notifyDataSetChanged()
        } else if (!historySearch.isEmpty()) {
            binding.youSearched.visibility = View.VISIBLE
            binding.clearHistory.visibility = View.VISIBLE
            adapter.trackList = historySearch
            adapter.notifyDataSetChanged()
        }
    }

    private fun checkIfTrackIsThere(track: Track){
            if (historySearch.contains(track)) {
                historySearch.remove(track)
            }
        historySearch.add(0, track)
    }

    private fun clearTrackList() {
        tracks.clear()
        adapter.notifyDataSetChanged()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_STATE, editTextState.toString())
        outState.putSerializable(SEARCH_STATE, tracksState)
    }

    companion object {
        const val SEARCH_STATE = "SEARCH_STATE"
        const val DEFAULT_STATE = ""
        val DEFAULT_TRACKS_STATE = ArrayList<Track>()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        editTextState = savedInstanceState.getString(SEARCH_STATE, DEFAULT_STATE)
        binding.inputSearch.setText(editTextState)
        tracksState = savedInstanceState.getSerializable(SEARCH_STATE) as ArrayList<Track>

    }
}