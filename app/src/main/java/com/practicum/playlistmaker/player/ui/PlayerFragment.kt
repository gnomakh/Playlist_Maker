package com.practicum.playlistmaker.player.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlayerBinding
import com.practicum.playlistmaker.media.domain.models.Playlist
import com.practicum.playlistmaker.media.ui.playlists.PlaylistsAdapter
import com.practicum.playlistmaker.media.ui.state.PlaylistsState
import com.practicum.playlistmaker.player.ui.ViewModel.PlayerViewModel
import com.practicum.playlistmaker.player.ui.state.PlaybackState
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerFragment : Fragment() {

    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!
    private val adapter = PlaylistsAdapter(R.layout.playlist_item_bottomsheet)

    private val gson = Gson()

    private val viewModel: PlayerViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.playerToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.newPlaylistButtonBottomsheet.setOnClickListener {
            findNavController().navigate(R.id.action_playerFragment_to_playlistCreationFragment)
        }


        val type = object : TypeToken<Track>() {}.type
        val track = gson.fromJson<Track>(arguments?.getString("trackForPlayer"), type)

        viewModel.initializeViewModel(track)

        viewModel.getTrackInfoLiveData().observe(viewLifecycleOwner) {
            setTrackInfo(it)
            binding.likeButton.setImageResource(
                when (it.isFavorite) {
                    true -> R.drawable.like_button_active
                    false -> R.drawable.like_button_unactive
                }
            )
        }

        viewModel.getPlaylists()
        viewModel.getPlaylistsLiveData()?.observe(viewLifecycleOwner) {
            setData(it)

        }

        binding.playButton.setOnClickListener {
            viewModel.playbackControl()
        }

        binding.likeButton.setOnClickListener {
            viewModel.onFavoriteClick()
        }

        viewModel.getPlayerStateLiveData().observe(viewLifecycleOwner) {
            binding.playButton.setImageResource(
                when (it) {
                    PlaybackState.PLAYING_STATE -> R.drawable.pause_button
                    PlaybackState.PAUSED_STATE -> R.drawable.play_button
                    PlaybackState.PREPARED_STATE -> R.drawable.play_button
                    PlaybackState.DEFAULT_STATE -> R.drawable.play_button
                }
            )
        }

        viewModel.getPlaybackTimeLiveData().observe(viewLifecycleOwner) {
            renderTimer(it)
        }

        binding.rvPlaylists.adapter = adapter

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.standardBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        binding.addToPlaylistButton.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {

                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.visibility = View.GONE
                    }

                    else -> {
                        binding.overlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.overlay.alpha = (slideOffset + 1)
            }
        })

        adapter.listener = PlaylistsAdapter.OnPlaylistClickListener { playlist ->

            lifecycleScope.launch {
                val added = viewModel.addToPlaylist(playlist)
                when (added) {
                    true -> {
                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                        Toast.makeText(
                            requireActivity(),
                            "Добавлено в плейлист ${playlist.title}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    false -> Toast.makeText(
                        requireActivity(),
                        "Трек уже добавлен в плейлист ${playlist.title}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                adapter.notifyDataSetChanged()
            }

        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.onAppPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onFragmentDestroy()
    }

    private fun renderTimer(currentTime: String) {
        binding.playbackTime.text = currentTime
    }

    private fun setTrackInfo(trackOnPlayer: Track) {
        Glide.with(this).load(trackOnPlayer.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(
                R.drawable.placeholder_player
            ).fitCenter()
            .transform(RoundedCorners((8 * resources.displayMetrics.density).toInt()))
            .into(binding.artworkCover)
        with(binding) {
            trackName.text = trackOnPlayer.trackName
            artistName.text = trackOnPlayer.artistName
            durationPl.text = trackOnPlayer.trackTimeMillis
            playbackTime.text = getString(R.string.duration_pl)
            albumPl.text = trackOnPlayer.collectionName
            yearPl.text = trackOnPlayer.releaseDate.substring(0, 4)
            genrePl.text = trackOnPlayer.primaryGenreName
            countryPl.text = trackOnPlayer.country
        }
    }

    private fun setData(data: PlaylistsState) {
        when (data) {
            is PlaylistsState.Empty -> {}
            is PlaylistsState.Content -> {
                adapter.playlists = data.playlists as ArrayList<Playlist>
            }
        }
        adapter.notifyDataSetChanged()
    }
}