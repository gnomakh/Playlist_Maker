package com.practicum.playlistmaker.search.ui.track

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.TrackItemBinding
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.root.dpToPx


class TracksHolder(private val trackItemBinding: TrackItemBinding) : RecyclerView.ViewHolder(trackItemBinding.root) {

    fun bind(model: Track) {
        trackItemBinding.trackName.text = model.trackName
        trackItemBinding.artistName.text = model.artistName
        trackItemBinding.trackDuration.text = model.trackTimeMillis
        val artwork = trackItemBinding.artworkCover

        Glide.with(itemView.context).load(model.artworkUrl100).placeholder(R.drawable.placeholder).fitCenter()
            .transform(RoundedCorners(itemView.context.dpToPx(2.0F))).into(artwork)
    }

}
