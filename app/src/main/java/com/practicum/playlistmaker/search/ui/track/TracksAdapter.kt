package com.practicum.playlistmaker.search.ui.track

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.databinding.TrackItemBinding
import com.practicum.playlistmaker.search.domain.models.Track

class TracksAdapter() : RecyclerView.Adapter<TracksHolder>() {

    var trackList = arrayListOf<Track>()
    var onClickListener: OnTrackClickListener? = null
    var onHoldListener: OnTrackHoldListener? = null

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
        holder.bind(track)
        holder.itemView.setOnClickListener {
            onClickListener?.onTrackClick(track)
        }

        holder.itemView.setOnLongClickListener {
            onHoldListener?.onTrackHold(track)
            true
        }
    }

    fun interface OnTrackClickListener {
        fun onTrackClick(track: Track)
    }
    fun interface OnTrackHoldListener {
        fun onTrackHold(track: Track)
    }

}