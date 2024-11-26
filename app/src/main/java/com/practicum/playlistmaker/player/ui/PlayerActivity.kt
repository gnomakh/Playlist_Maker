package com.practicum.playlistmaker.player.ui

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityPlayerBinding
import com.practicum.playlistmaker.player.ui.ViewModel.PlayerViewModel
import com.practicum.playlistmaker.player.ui.state.PlaybackState
import com.practicum.playlistmaker.root.dpToPx
import com.practicum.playlistmaker.search.domain.models.Track
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding
    private val viewModel: PlayerViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = LayoutInflater.from(this)
        binding = ActivityPlayerBinding.inflate(inflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            finish()
        }

        viewModel.getTrackInfoLiveData().observe(this) {
            setTrackInfo(it)
        }

        binding.playButton.setOnClickListener {
            viewModel.playbackControl()
        }

        viewModel.getPlayerStateLiveData().observe(this) {
            binding.playButton.setImageResource(
                when (it) {
                    PlaybackState.PLAYING_STATE -> R.drawable.pause_button
                    PlaybackState.PAUSED_STATE -> R.drawable.play_button
                    PlaybackState.PREPARED_STATE -> R.drawable.play_button
                    PlaybackState.DEFAULT_STATE -> R.drawable.play_button
                }
            )
        }

        viewModel.getPlaybackTimeLiveData().observe(this) {
            renderTimer(it)
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.onActivityPause()
    }

    private fun renderTimer(currentTime: String) {
        binding.playbackTime.text = currentTime
    }

    private fun setTrackInfo(trackOnPlayer: Track) {
        Glide.with(this).load(trackOnPlayer.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(
                R.drawable.placeholder_player
            ).fitCenter()
            .transform(RoundedCorners(this.dpToPx(8.0F))).into(binding.artworkCover)
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