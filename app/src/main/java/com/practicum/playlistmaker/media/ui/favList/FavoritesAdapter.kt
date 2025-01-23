package com.practicum.playlistmaker.media.ui.favList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.databinding.TrackItemBinding
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.ui.track.TracksHolder

class FavoritesAdapter : RecyclerView.Adapter<TracksHolder>() {

    var trackList = arrayListOf<Track>()
    var listener: OnTrackClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = TrackItemBinding.inflate(inflater, parent, false)
        return TracksHolder(binding)
    }

    override fun getItemCount(): Int {
        return trackList.size
    }

    override fun onBindViewHolder(holder: TracksHolder, position: Int) {
        val track = trackList[position]
        holder.bind(trackList[position])
        holder.itemView.setOnClickListener {
            listener?.onTrackClick(track)
        }
    }

    fun interface OnTrackClickListener {
        fun onTrackClick(track: Track)
    }
}