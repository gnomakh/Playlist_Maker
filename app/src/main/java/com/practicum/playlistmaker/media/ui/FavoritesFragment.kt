package com.practicum.playlistmaker.media.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.bundle.Bundle
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.databinding.FragmentFavoritesBinding
import com.practicum.playlistmaker.media.ui.ViewModel.FavoritesViewModel
import com.practicum.playlistmaker.media.ui.favList.FavoritesAdapter
import com.practicum.playlistmaker.media.ui.state.FavoritesState
import com.practicum.playlistmaker.search.domain.models.Track
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FavoritesViewModel by viewModel()
    private val adapter = FavoritesAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getFavorites().observe(viewLifecycleOwner) {
            setData(it)
        }
        binding.rvFavorites.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setData(data: FavoritesState) {
        when(data) {
            is FavoritesState.Empty -> {
                binding.favImgPlaceholder.isVisible = true
                binding.favPlaceholderMessage.isVisible = true
            }
            is FavoritesState.Content -> {
                binding.favImgPlaceholder.isVisible = false
                binding.favPlaceholderMessage.isVisible = false
                adapter.trackList = data.favorites as ArrayList<Track>
                adapter.notifyDataSetChanged()
            }
        }
    }

    companion object {
        fun newInstance() = FavoritesFragment()
    }

}