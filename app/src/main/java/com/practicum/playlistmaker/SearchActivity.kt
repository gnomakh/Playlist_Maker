package com.practicum.playlistmaker

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.network.ITunesAPI
import com.practicum.playlistmaker.network.SearchResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {
    var editTextState: CharSequence? = DEFAULT_STATE

    private lateinit var binding: ActivitySearchBinding

    private val tracksBaseUrl = "https://itunes.apple.com"

    private val retrofit =
        Retrofit.Builder()
            .baseUrl(tracksBaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    private val trackService = retrofit.create(ITunesAPI::class.java)

    private lateinit var backButton: ImageView
    private lateinit var inputEditText: EditText
    private lateinit var clearButton: ImageView
    private lateinit var rvTrackList: RecyclerView

    private val tracks = ArrayList<Track>()

    private var adapter = TracksAdapter(tracks)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = LayoutInflater.from(this)
        binding = ActivitySearchBinding.inflate(inflater)
        setContentView(R.layout.activity_search)

        backButton = binding.backButton
        inputEditText = binding.inputSearch
        clearButton = binding.clearButton

        backButton.setOnClickListener {
            finish()
        }

        clearButton.setOnClickListener {
            inputEditText.setText("")
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }

        inputEditText.doOnTextChanged { text, start, before, count ->
            if (text.isNullOrEmpty()) {
                clearButton.visibility = View.GONE
            } else {
                clearButton.visibility = View.VISIBLE
            }
        }

        rvTrackList = binding.rvTracks
        rvTrackList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvTrackList.adapter = adapter

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                trackService.search(binding.inputSearch.text.toString())
                    .enqueue(object : Callback<SearchResponse> {
                        override fun onResponse(
                            call: Call<SearchResponse>, response: Response<SearchResponse>
                        ) {
                            when (response.code()) {
                                200 -> {
                                    tracks.clear()
                                    if (response.body()?.results?.isNotEmpty() == true) {
                                        tracks.addAll(response.body()?.results!!)
                                        adapter.notifyDataSetChanged()
                                    } else {
                                        doNothing()
                                    }
                                }

                                else -> {
                                    doNothing()
                                }
                            }
                        }

                        override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                            doNothing()
                        }

                    })
            }
            false
        }
    }

    fun doNothing() {}

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_STATE, editTextState.toString())
    }

    companion object {
        const val SEARCH_STATE = "SEARCH_STATE"
        const val DEFAULT_STATE = ""
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        editTextState = savedInstanceState.getString(SEARCH_STATE, DEFAULT_STATE)
        binding.inputSearch.setText(editTextState)
    }
}