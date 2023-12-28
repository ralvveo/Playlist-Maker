package com.practicum.playlistmaker

import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.bitmap.TransformationUtils.fitCenter

class TracksViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val trackImage: ImageView = itemView.findViewById(R.id.trackItemImage)
    private val trackName: TextView = itemView.findViewById(R.id.trackItemName)
    private val trackArtistName: TextView = itemView.findViewById(R.id.trackItemArtistName)
    private val trackTime: TextView = itemView.findViewById(R.id.trackItemTime)

    fun bind(model: Track) {
        trackName.text = model.trackName
        trackArtistName.text = model.artistName
        trackTime.text = model.trackTime
        val roundedCornersSize: Int = 8
        Glide.with(itemView)
            .load(model.artworkUrl100)
            .transform(FitCenter(), RoundedCorners(roundedCornersSize))
            .placeholder(R.drawable.track_image_placeholder)
            .into(trackImage)
    }


}