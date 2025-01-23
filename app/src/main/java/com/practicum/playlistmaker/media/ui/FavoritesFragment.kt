package com.practicum.playlistmaker.media.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.bundle.Bundle
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentFavoritesBinding
import com.practicum.playlistmaker.media.ui.ViewModel.FavoritesViewModel
import com.practicum.playlistmaker.media.ui.favList.FavoritesAdapter
import com.practicum.playlistmaker.media.ui.state.FavoritesState
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FavoritesViewModel by viewModel()
    private val adapter = FavoritesAdapter()
    private val gson = Gson()
    private var clickCurrentState = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clickCurrentState = true
        binding.rvFavorites.adapter = adapter
        viewModel.getFavoritesList()
        viewModel.getFavoritesLivedLiveData().observe(viewLifecycleOwner) {
            setData(it)
        }


        val navHostFragment =
            activity?.supportFragmentManager?.findFragmentById(R.id.container_view) as NavHostFragment
        val navController = navHostFragment.navController
        adapter.listener = FavoritesAdapter.OnTrackClickListener { track ->
            if (debounceClick()) {
                val gsonTrack = gson.toJson(track)
                val bundle = android.os.Bundle()
                bundle.putString("trackForPlayer", gsonTrack)

                navController.navigate(R.id.action_mediaFragment_to_playerFragment, bundle)
            }
        }
    }

    override fun onPause() {
        super.onPause()

        viewModel.getFavoritesList()
        adapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setData(data: FavoritesState) {
        when (data) {
            is FavoritesState.Empty -> {
                binding.rvFavorites.isVisible = false
                binding.favImgPlaceholder.isVisible = true
                binding.favPlaceholderMessage.isVisible = true
            }

            is FavoritesState.Content -> {
                binding.rvFavorites.isVisible = true
                binding.favImgPlaceholder.isVisible = false
                binding.favPlaceholderMessage.isVisible = false
                adapter.trackList = data.favorites as ArrayList<Track>
            }
        }
        adapter.notifyDataSetChanged()
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
        fun newInstance() = FavoritesFragment()
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

}