package com.practicum.playlistmaker.media.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.GridLayoutManager
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistsBinding
import com.practicum.playlistmaker.media.domain.models.Playlist
import com.practicum.playlistmaker.media.ui.ViewModel.PlaylistsViewModel
import com.practicum.playlistmaker.media.ui.playlists.PlaylistsAdapter
import com.practicum.playlistmaker.media.ui.state.PlaylistsState
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {
    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!
    private val adapter = PlaylistsAdapter(R.layout.playlist_item_media)

    private val viewModel: PlaylistsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvPlaylists.layoutManager = GridLayoutManager(requireActivity(), 2)
        binding.rvPlaylists.adapter = adapter

        viewModel.getPlaylists()
        viewModel.getPlaylistsLiveData()?.observe(viewLifecycleOwner) {
            setData(it)
        }


        val navHostFragment =
            activity?.supportFragmentManager?.findFragmentById(R.id.container_view) as NavHostFragment
        val navController = navHostFragment.navController


        binding.newPlaylistButton.setOnClickListener {
            navController.navigate(R.id.action_mediaFragment_to_playlistCreationFragment)
        }

        adapter.listener = PlaylistsAdapter.OnPlaylistClickListener { playlist ->
            val bundle = Bundle()
            bundle.putString("playlistId", playlist.id.toString())
            navController.navigate(R.id.action_mediaFragment_to_playlistFragment, bundle)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setData(data: PlaylistsState) {
        when (data) {
            is PlaylistsState.Empty -> togglePlaceholders(PH_ON)
            is PlaylistsState.Content -> {
                togglePlaceholders(PH_OFF)
                adapter.playlists = data.playlists as ArrayList<Playlist>
            }
        }
        adapter.notifyDataSetChanged()
    }

    private fun togglePlaceholders(state: String) {
        when (state) {
            PH_OFF -> {
                binding.rvPlaylists.isVisible = true
                binding.playlistImgPlaceholder.isVisible = false
                binding.playlistsPlaceholderMessage.isVisible = false
            }

            PH_ON -> {
                binding.rvPlaylists.isVisible = false
                binding.playlistImgPlaceholder.isVisible = true
                binding.playlistsPlaceholderMessage.isVisible = true
            }
        }
    }

    companion object {
        fun newInstance() = PlaylistsFragment()
        const val PH_OFF = "off"
        const val PH_ON = "on"
    }
}