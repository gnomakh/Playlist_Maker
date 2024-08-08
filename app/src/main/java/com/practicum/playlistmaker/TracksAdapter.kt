package com.practicum.playlistmaker

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.databinding.TrackItemBinding

class TracksAdapter(prefs: SharedPreferences) : RecyclerView.Adapter<TracksHolder>() {
    private var historyIsShown = false
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
            if (!historyIsShown) prefCon.saveTrackToPref(trackList[position])
        }
    }

    fun setHistoryShown(isShown: Boolean) {
        historyIsShown = isShown
    }
}