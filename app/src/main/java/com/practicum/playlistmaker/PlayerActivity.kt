package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.databinding.ActivityPlayerBinding
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var prefConv: PrefGsonConvert

    private companion object {
        const val DEFAULT_STATE = "DEFAULT"
        const val PREPARED_STATE = "PREPARED"
        const val PLAYING_STATE = "PLAYING"
        const val PAUSED_STATE = "PAUSED"
        const val timeUpdateDelay = 333L
    }

    private var playerState = DEFAULT_STATE
    private var mediaPlayer = MediaPlayer()
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = LayoutInflater.from(this)
        binding = ActivityPlayerBinding.inflate(inflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences(PrefGsonConvert.PREFS_KEY, MODE_PRIVATE)
        prefConv = PrefGsonConvert(sharedPreferences)

        val trackOnPlayer = prefConv.getTrackFromPref() ?: return finish()
        val trackUrl = trackOnPlayer.previewUrl

        preparePlayer(trackUrl)

        binding.backButton.setOnClickListener {
            finish()
        }

        Glide.with(this).load(trackOnPlayer.artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")).placeholder(R.drawable.placeholder_player).fitCenter()
            .transform(RoundedCorners(this.dpToPx(8.0F))).into(binding.artworkCover)

        with(binding) {
            trackName.text = trackOnPlayer.trackName
            artistName.text = trackOnPlayer.artistName
            durationPl.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackOnPlayer.trackTimeMillis)
            playbackTime.text = "00:00"
            albumPl.text = trackOnPlayer.collectionName
            yearPl.text = trackOnPlayer.releaseDate.substring(0, 4)
            genrePl.text = trackOnPlayer.primaryGenreName
            countryPl.text = trackOnPlayer.country
        }

        binding.playButton.setOnClickListener {
            playbackControl()
        }
    }

    override fun onPause() {
        super.onPause()
        if (playerState == PLAYING_STATE) {
            pausePlayer()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    private fun preparePlayer(trackUrl: String) {
        mediaPlayer.setDataSource(trackUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            binding.playButton.isEnabled = true
            playerState = PREPARED_STATE
        }
        mediaPlayer.setOnCompletionListener {
            binding.playButton.setImageResource(R.drawable.play_button)
            playerState = PREPARED_STATE
        }
    }

    @SuppressLint("ResourceType")
    private fun startPlayer() {
        mediaPlayer.start()
        playerState = PLAYING_STATE
        updateTimer()
        binding.playButton.setImageResource(R.drawable.pause_button)
    }

    @SuppressLint("ResourceType")
    private fun pausePlayer() {
        mediaPlayer.pause()
        playerState = PAUSED_STATE
        binding.playButton.setImageResource(R.drawable.play_button)
    }

    private fun playbackControl() {
        when(playerState) {
            PLAYING_STATE -> {
                pausePlayer()
            }
            PAUSED_STATE, PREPARED_STATE -> {
                startPlayer()
            }
        }
    }

    private fun updateTimer() {
        handler.postDelayed(
            object : Runnable {
                override fun run() {
                    if (playerState == PLAYING_STATE) {
                        binding.playbackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
                        handler.postDelayed(
                            this, timeUpdateDelay
                        )
                    } else if (playerState == PREPARED_STATE) {
                        binding.playbackTime.text = "00:00"
                    }
                }
            }, timeUpdateDelay
        )
    }
}