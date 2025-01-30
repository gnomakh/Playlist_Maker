package com.practicum.playlistmaker.media.ui.playlists

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.media.domain.models.Playlist
import com.practicum.playlistmaker.root.dpToPx
import com.practicum.playlistmaker.root.getDeclination

class PlaylistsHolder(view: View, layoutId: Int) :
    RecyclerView.ViewHolder(view) {
    private val title: TextView = view.findViewById(R.id.playlist_title)
    private val trackCount: TextView = view.findViewById(R.id.track_count)
    private val picture: ImageView = view.findViewById(R.id.playlist_picture)

    val dpValue = if (layoutId == R.layout.playlist_item_media) 8.0F else 2.0F

    fun bind(playlist: Playlist) {

        title.text = playlist.title
        trackCount.text =
            "${playlist.tracksCount} ${itemView.context.getDeclination(playlist.tracksCount)}"
        if (playlist.picture != "") {
            Glide.with(itemView.context).load(playlist.picture)
                .transform(
                    CenterCrop(),
                    RoundedCorners(itemView.context.dpToPx(dpValue)),
                )
                .placeholder(R.drawable.placeholder_player)
                .into(picture)
        }
    }
}