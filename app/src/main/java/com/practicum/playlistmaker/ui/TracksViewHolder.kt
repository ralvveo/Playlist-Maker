package com.practicum.playlistmaker.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.TrackItemBinding
import com.practicum.playlistmaker.domain.model.Track
import java.text.SimpleDateFormat
import java.util.Locale

class TracksViewHolder(private val binding: TrackItemBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(model: Track) {
        binding.trackItemName.text = model.trackName
        binding.trackItemArtistName.text = model.artistName
        binding.trackItemTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(model.trackTime)
        val roundedCornersSize: Int = 8
        Glide.with(itemView)
            .load(model.artworkUrl100)
            .transform(FitCenter(), RoundedCorners(roundedCornersSize))
            .placeholder(R.drawable.track_image_placeholder)
            .into(binding.trackItemImage)
    }


}