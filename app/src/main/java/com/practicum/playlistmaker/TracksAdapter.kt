package com.practicum.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TracksAdapter(private val trackList: List<Track>) : RecyclerView.Adapter<TracksHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksHolder {
        return TracksHolder(LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false))
    }

    override fun getItemCount(): Int {
        return trackList.size
    }

    override fun onBindViewHolder(holder: TracksHolder, position: Int) {
        holder.bind(trackList[position])
    }
}