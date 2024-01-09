package com.practicum.playlistmaker

import android.app.Application
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.App.Companion.trackHistoryList

class TracksAdapter(
    private val tracks: List<Track>
) : RecyclerView.Adapter<TracksViewHolder> () {

    val searchHistory = SearchHistory(null)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        return TracksViewHolder(view)
    }

    override fun onBindViewHolder(holder: TracksViewHolder, position: Int) {
        holder.bind(tracks[position])

        holder.itemView.setOnClickListener {
            searchHistory.addTrackToHistory(tracks[position])

        }


    }

    override fun getItemCount(): Int {
        return tracks.size
    }

}