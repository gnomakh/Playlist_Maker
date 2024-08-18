package com.practicum.playlistmaker

import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import android.view.LayoutInflater
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = LayoutInflater.from(this)
        binding = ActivityPlayerBinding.inflate(inflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences(PrefGsonConvert.PREFS_KEY, MODE_PRIVATE)
        prefConv = PrefGsonConvert(sharedPreferences)

        val trackOnPlayer = prefConv.getTrackFromPref()

        binding.backButton.setOnClickListener {
            finish()
        }

        Glide.with(this).load(trackOnPlayer?.artworkUrl100?.replaceAfterLast('/',"512x512bb.jpg")).placeholder(R.drawable.placeholder_player).fitCenter()
            .transform(RoundedCorners(this.dpToPx(8.0F))).into(binding.artworkCover)

        binding.trackName.text = trackOnPlayer?.trackName
        binding.artistName.text = trackOnPlayer?.artistName
        binding.durationPl.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackOnPlayer?.trackTimeMillis)
        binding.timeUnderButton.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackOnPlayer?.trackTimeMillis)
        binding.albumPl.text = trackOnPlayer?.collectionName
        binding.yearPl.text = trackOnPlayer?.releaseDate?.substring(0, 4)
        binding.genrePl.text = trackOnPlayer?.primaryGenreName
        binding.countryPl.text = trackOnPlayer?.country
    }

}