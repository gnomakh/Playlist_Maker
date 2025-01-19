package com.practicum.playlistmaker.media.ui.playlists

import android.os.Environment
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.PlaylistItemMediaBinding
import com.practicum.playlistmaker.media.domain.models.Playlist
import com.practicum.playlistmaker.root.dpToPx
import java.io.File

class PlaylistsHolder(private val playlistItemMediaBinding: PlaylistItemMediaBinding) :
    RecyclerView.ViewHolder(playlistItemMediaBinding.root) {
    private val title: TextView = playlistItemMediaBinding.playlistTitle
    private val description: TextView = playlistItemMediaBinding.trackCount
    private val picture: ImageView = playlistItemMediaBinding.playlistPicture

    fun bind(playlist: Playlist) {
        title.text = playlist.title
        description.text = playlist.tracksCount
        if (playlist.picture != "") {
            val filePath = File(itemView.context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "pl_covers")
            val file = File(filePath, "${playlist.picture}")
            Glide.with(itemView.context).load(file.toUri())
                .transform(
                    CenterCrop(),
                    RoundedCorners(itemView.context.dpToPx(8.0F)),
                )
                .placeholder(R.drawable.placeholder_player)
                .into(picture)
        }
    }
}