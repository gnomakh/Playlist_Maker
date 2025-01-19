package com.practicum.playlistmaker.media.ui.playlists

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.databinding.PlaylistItemMediaBinding
import com.practicum.playlistmaker.media.domain.models.Playlist

class PlaylistsAdapter() : RecyclerView.Adapter<PlaylistsHolder>() {

    var playlists = arrayListOf<Playlist>()
    var listener: OnPlaylistClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistsHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PlaylistItemMediaBinding.inflate(inflater, parent, false)
        return PlaylistsHolder(binding)
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    override fun onBindViewHolder(holder: PlaylistsHolder, position: Int) {
        val playlist = playlists[position]
        holder.bind(playlist)
        holder.itemView.setOnClickListener{
            listener?.onPlaylistClick(playlist)
        }
    }

    fun interface OnPlaylistClickListener {
        fun onPlaylistClick(playlist: Playlist)
    }
}