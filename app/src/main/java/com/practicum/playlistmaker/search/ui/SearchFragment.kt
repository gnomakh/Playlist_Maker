package com.practicum.playlistmaker.search.ui

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentSearchBinding
import com.practicum.playlistmaker.root.RootActivity
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.ui.ViewModel.SearchViewModel
import com.practicum.playlistmaker.search.ui.state.SearchScreenState
import com.practicum.playlistmaker.search.ui.track.TracksAdapter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private val adapter: TracksAdapter = TracksAdapter()

    private var clickCurrentState = true
    private var searchQueue = ""

    private val viewModel: SearchViewModel by viewModel()

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val gson = Gson()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clickCurrentState = true

        binding.clearHistory.setOnClickListener {
            viewModel.clearHistory()
            toggleOffHistory()
        }

        viewModel.getScreenStateLiveData().observe(viewLifecycleOwner) {
            render(it)
        }

        binding.updateButton.setOnClickListener {
            viewModel.retrySearch()
        }

        binding.clearButton.setOnClickListener {
            binding.inputSearch.setText("")
            clearTrackList()
            val imm = requireContext().getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(it.windowToken, 0)
        }

        binding.inputSearch.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus and binding.inputSearch.text.isEmpty()) {
                viewModel.renderHistory()
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
                    viewModel.cancelJob()
                }
                if (binding.inputSearch.hasFocus() && text?.isEmpty() == true) {
                    viewModel.renderHistory()
                } else {
                    viewModel.renderTracks()
                }
            },
            afterTextChanged = { text: Editable? -> }
        )

        val navHostFragment =
            activity?.supportFragmentManager?.findFragmentById(R.id.container_view) as NavHostFragment
        val navController = navHostFragment.navController

        adapter.listener = TracksAdapter.OnTrackClickListener { track ->
            if (debounceClick()) {
                viewModel.addTrackToHistory(track)
                if (viewModel.getScreenStateLiveData().value is SearchScreenState.History)
                    viewModel.renderHistory()
                adapter.notifyDataSetChanged()

                val gsonTrack = gson.toJson(track)
                val bundle = Bundle()
                bundle.putString("trackForPlayer", gsonTrack)

                navController.navigate(R.id.action_searchFragment_to_playerFragment, bundle)
            }
        }
        adapter.trackList = arrayListOf()
        binding.rvTracks.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun render(state: SearchScreenState) {
        when (state) {
            is SearchScreenState.Loading -> showLoading()
            is SearchScreenState.Tracks -> showTracks(state.tracksSearch)
            is SearchScreenState.History -> toggleOnHistory(state.historyList)
            is SearchScreenState.EmptyResult -> showErrorPlaceholder(ResponseCode.NO_RESULT)
            is SearchScreenState.NetwotkError -> showErrorPlaceholder(ResponseCode.NETWORK_ERROR)
            is SearchScreenState.Nothing -> {
                hideLoading()
                toggleOffHistory()
                toggleOffPlaceholders()
                viewModel.clearTrackList()
            }
        }
    }

    private fun showTracks(trackList: List<Track>) {
        toggleOffPlaceholders()
        toggleOffHistory()
        hideLoading()
        binding.rvTracks.isVisible = true
        adapter.trackList = trackList as ArrayList<Track>
        adapter.notifyDataSetChanged()
    }

    private fun showErrorPlaceholder(errorCode: ResponseCode) {
        toggleOffHistory()
        hideLoading()
        when (errorCode) {
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

    private fun toggleOnHistory(historyList: List<Track>) {
        toggleOffPlaceholders()
        hideLoading()
        val history = historyList
        if (history.isNotEmpty()) {
            binding.rvTracks.isVisible = true
            binding.youSearched.isVisible = true
            binding.clearHistory.isVisible = true
            adapter.trackList = history as ArrayList<Track>
            adapter.notifyDataSetChanged()
        }
    }

    private fun toggleOffHistory() {
        binding.youSearched.isVisible = false
        binding.clearHistory.isVisible = false
        adapter.trackList = arrayListOf()
        adapter.notifyDataSetChanged()
    }

    private fun clearTrackList() {
        viewModel.clearTrackList()
        viewModel.renderHistory()
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
        val isClickAllowed = clickCurrentState
        if (clickCurrentState) {
            clickCurrentState = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                clickCurrentState = true
            }
        }
        return isClickAllowed
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    enum class ResponseCode { NO_RESULT, NETWORK_ERROR }
}