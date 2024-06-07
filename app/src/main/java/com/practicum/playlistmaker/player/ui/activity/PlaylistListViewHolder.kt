package com.practicum.playlistmaker.player.ui.activity

import android.os.Environment
import android.util.Log
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

class PlaylistListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val playlistImage: ImageView = itemView.findViewById(R.id.playlistImage)
    private val playlistName: TextView = itemView.findViewById(R.id.playlistName)
    private val playlistTrackCount: TextView = itemView.findViewById(R.id.playlistTrackCount)



    fun bind(model: Playlist) {
        playlistName.text = model.playlistName
        playlistTrackCount.text = "${model.playlistTrackCount} ${doCorrectEnding(model.playlistTrackCount.toInt())}"
        Log.d("PLAYLISTID", "${model.playlistId}")
        val fileCodeName = model.playlistImage.toString().takeLast(10)
        val filePath = File(playlistName.context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum")
        val file = File(filePath, "$fileCodeName.jpg")
        val roundedCornersSize = 8
        Glide.with(itemView)
            .load(file.toUri())
            .transform(CenterCrop(), RoundedCorners(roundedCornersSize))
            .placeholder(R.drawable.playlist_placeholder)
            .into(playlistImage)
    }

    private fun doCorrectEnding(tracksCount: Int): String{
        return if (tracksCount in 11..14) "треков"
        else{
            when (tracksCount % 10){
                1 -> "трек"
                2,3,4 -> "трека"
                else -> "треков"
            }
        }
    }

}