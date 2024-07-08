package com.practicum.playlistmaker

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners


class TracksHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val trackName: TextView
    private val artistName: TextView
    private val trackDuration: TextView
    private val artwork: ImageView

    init {
        trackName = itemView.findViewById(R.id.trackName)
        artistName = itemView.findViewById(R.id.artistName)
        trackDuration = itemView.findViewById(R.id.trackDuration)
        artwork = itemView.findViewById(R.id.artworkCover)
    }

    fun bind(model: Track) {
        trackName.text = model.trackName
        artistName.text = model.artistName
        trackDuration.text = model.trackTime
        Glide.with(itemView.context).load(model.artworkUrl100).placeholder(R.drawable.placeholder).fitCenter()
            .transform(RoundedCorners(dpToPx(2.0F, itemView.context))).into(artwork)
    }

    fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }
}
