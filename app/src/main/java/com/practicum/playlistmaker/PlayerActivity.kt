package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.databinding.ActivityPlayerBinding
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var prefConv: PrefGsonConvert

    companion object {
        private const val DEFAULT_STATE = "DEFAULT"
        private const val PREPARED_STATE = "PREPARED"
        private const val PLAYING_STATE = "PLAYING"
        private const val PAUSED_STATE = "PAUSED"
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

        if(trackUrl != null) {
            preparePlayer(trackUrl)
        } else {
            Toast.makeText(this, "Не удалось получить ссылку на отрывок", Toast.LENGTH_SHORT).show()
        }

        binding.backButton.setOnClickListener {
            finish()
        }

        Glide.with(this).load(trackOnPlayer.artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")).placeholder(R.drawable.placeholder_player).fitCenter()
            .transform(RoundedCorners(this.dpToPx(8.0F))).into(binding.artworkCover)

        with(binding) {
            trackName.text = trackOnPlayer.trackName
            artistName.text = trackOnPlayer.artistName
            durationPl.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackOnPlayer.trackTimeMillis)
            timeUnderButton.text = "00:00"
            albumPl.text = trackOnPlayer.collectionName
            yearPl.text = trackOnPlayer.releaseDate.substring(0, 4)
            genrePl.text = trackOnPlayer.primaryGenreName
            countryPl.text = trackOnPlayer.country
        }

        binding.playButton.setOnClickListener {
            playbackControl()
            updateTimer()
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
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
                    binding.timeUnderButton.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
                    if (playerState == PLAYING_STATE) {
                        handler.postDelayed(
                            this, 333
                        )
                    } else if (playerState == PREPARED_STATE) {
                        binding.timeUnderButton.text = "00:00"
                    }
                }
            }, 333
        )
    }
}