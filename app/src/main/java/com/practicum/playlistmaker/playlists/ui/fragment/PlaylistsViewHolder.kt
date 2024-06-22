package com.practicum.playlistmaker.playlists.ui.fragment

import android.os.Environment
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.playlists.domain.model.Playlist
import java.io.File

class PlaylistsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val playlistImage: ImageView = itemView.findViewById(R.id.playlistImage)
    private val playlistName: TextView = itemView.findViewById(R.id.playlistName)
    private val playlistTrackCount: TextView = itemView.findViewById(R.id.playlistTrackCount)



    fun bind(model: Playlist) {
        playlistName.text = model.playlistName
        playlistTrackCount.text = itemView.resources.getQuantityString(R.plurals.plurals_tracks, model.playlistTrackCount.toInt(), model.playlistTrackCount.toInt())

        val fileCodeName = model.playlistImage.toString().takeLast(10)
        val filePath = File(playlistName.context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), MY_ALBUM)
        val file = File(filePath, "$fileCodeName.jpg")
        val roundedCornersSize = 8
        Glide.with(itemView)
            .load(file.toUri())
            .transform(CenterCrop(), RoundedCorners(roundedCornersSize))
            .placeholder(R.drawable.playlist_placeholder)
            .into(playlistImage)


    }

    companion object{
        const val MY_ALBUM = "myalbum"
    }



}