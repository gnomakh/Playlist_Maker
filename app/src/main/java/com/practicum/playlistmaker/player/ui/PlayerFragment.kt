package com.practicum.playlistmaker.player.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlayerBinding
import com.practicum.playlistmaker.player.ui.ViewModel.PlayerViewModel
import com.practicum.playlistmaker.player.ui.state.PlaybackState
import com.practicum.playlistmaker.root.RootActivity
import com.practicum.playlistmaker.search.domain.models.Track
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerFragment : Fragment() {

    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!

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
            requireActivity().onBackPressedDispatcher.onBackPressed()
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
            .transform(RoundedCorners((8 * resources.displayMetrics.density).toInt())).into(binding.artworkCover)
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
}