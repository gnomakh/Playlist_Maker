package com.practicum.playlistmaker

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.databinding.TrackItemBinding

class TracksAdapter(prefs: SharedPreferences) : RecyclerView.Adapter<TracksHolder>() {

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    var trackList = ArrayList<Track>()

    private val prefCon = PrefGsonConvert(prefs)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = TrackItemBinding.inflate(inflater, parent, false)
        return TracksHolder(binding)
    }

    override fun getItemCount(): Int {
        return trackList.size
    }

    override fun onBindViewHolder(holder: TracksHolder, position: Int) {
        holder.bind(trackList[position])

        holder.itemView.setOnClickListener {

            prefCon.saveTrackToPref(trackList[position])

            if(clickDebounce()) {
                val intent = Intent(holder.itemView.context as Activity, PlayerActivity::class.java)
                startActivity(holder.itemView.context as Activity, intent, null)
            }
        }
    }

    private var isClickAllowed = true

    private val handler = Handler(Looper.getMainLooper())

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }
}