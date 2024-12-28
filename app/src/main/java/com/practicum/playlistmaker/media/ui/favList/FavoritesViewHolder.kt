package com.practicum.playlistmaker.media.ui.favList

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.TrackItemBinding
import com.practicum.playlistmaker.root.dpToPx
import com.practicum.playlistmaker.search.domain.models.Track

class FavoritesViewHolder(private val trackItemBinding: TrackItemBinding) : RecyclerView.ViewHolder(trackItemBinding.root) {

    fun bind(model: Track) {
        trackItemBinding.trackName.text = model.trackName
        trackItemBinding.artistName.text = model.artistName
        trackItemBinding.trackDuration.text = model.trackTimeMillis
        val artwork = trackItemBinding.artworkCover

        Glide.with(itemView.context).load(model.artworkUrl100).placeholder(R.drawable.placeholder).fitCenter()
            .transform(RoundedCorners(itemView.context.dpToPx(2.0F))).into(artwork)
    }

}