package com.practicum.playlistmaker.player.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.databinding.ActivityPlayerBinding
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.PlayerListener
import com.practicum.playlistmaker.main.ui.dpToPx
import com.practicum.playlistmaker.player.ui.ViewModel.PlayerViewModel

class PlayerActivity : ComponentActivity() {

    private lateinit var binding: ActivityPlayerBinding
    private lateinit var trackOnPlayer: Track
    private lateinit var timeUpdateRunnable: Runnable
    private var handler: Handler? = null
    private val playerInteractor = Creator.providePlayerInteractor()


    private lateinit var viewModel: PlayerViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = LayoutInflater.from(this)
        binding = ActivityPlayerBinding.inflate(inflater)
        setContentView(binding.root)

        handler = Handler(Looper.getMainLooper())
        timeUpdateRunnable = createUpdateTimerRunnable()
        viewModel = ViewModelProvider(this)[PlayerViewModel::class.java]

        setTrackInfo()

        binding.backButton.setOnClickListener {
            finish()
        }


        val trackUrl = trackOnPlayer.previewUrl
        setTrackInfo(trackOnPlayer)
        playerInteractor.preparePlayer(trackUrl, this)
        binding.playButton.setOnClickListener {
            playerInteractor.playbackControl(this)
        }
    }

    override fun onPause() {
        super.onPause()
        playerInteractor.pausePlayer(this)

    }

    override fun onDestroy() {
        super.onDestroy()
        handler?.removeCallbacksAndMessages(null)
        playerInteractor.releaseMediaPlayer()
    }

    private fun setTrackInfo(trackOnPlayer: Track) {
        Glide.with(this).load(trackOnPlayer.artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")).placeholder(
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

    override fun onPlayerStop() {
        binding.playButton.setImageResource(R.drawable.play_button)
        handler?.removeCallbacks(timeUpdateRunnable)
        binding.playbackTime.text = getString(R.string.duration_pl)
    }

    override fun onPlayerStart() {
        binding.playButton.setImageResource(R.drawable.pause_button)
        handler?.post(timeUpdateRunnable)
    }

    override fun onPlayerPause() {
        handler?.removeCallbacks(timeUpdateRunnable)
        binding.playButton.setImageResource(R.drawable.play_button)
    }

    private fun createUpdateTimerRunnable(): Runnable {
        return object : Runnable {
            override fun run() {
                binding.playbackTime.text = playerInteractor.getCurrentTime()
                handler?.postDelayed(this, TIMER_DELAY)
            }
        }
    }

    companion object {
        private const val TIMER_DELAY = 333L
    }
}